package org.sgine.render.font

import java.net.URL

import org.lwjgl.opengl.GL11._

import org.sgine.core.Resource
import org.sgine.core.HorizontalAlignment
import org.sgine.core.VerticalAlignment

import org.sgine.render.Texture
import org.sgine.render.TextureMap
import org.sgine.render.TextureManager
import org.sgine.render.shape.MutableShape
import org.sgine.render.shape.Quad
import org.sgine.render.shape.ShapeMode
import org.sgine.render.shape.TextureData
import org.sgine.render.shape.VertexData

import scala.io.Source

import scala.collection.mutable.ArrayBuffer

import simplex3d.math.doublem.renamed._

class BitmapFont private[font](texture: Texture) extends TextureMap[Int, BitmapFontChar](texture)((quad: Quad) => new BitmapFontChar(quad)) with Font {
	private var _face: String = null
	private var _size: Double = 0.0
	private var _bold: Int = 0
	private var _italic: Int = 0
	private var _charset: String = ""
	private var _unicode: Int = 0
	private var _stretchH: Int = 0
	private var _smooth: Int = 0
	private var _aa: Int = 0
	private var _padding: Array[Int] = new Array[Int](4)
	private var _spacing: Array[Int] = new Array[Int](2)
	private var _lineHeight: Int = 0
	private var _base: Int = 0
	private var _scaleW: Int = 0
	private var _scaleH: Int = 0
	private var _pages: Int = 0
	private var _packed: Int = 0
	
	def face = _face
	def size = _size
	def bold = _bold
	def italic = _italic
	def charset = _charset
	def unicode = _unicode
	def stretchH = _stretchH
	def smooth = _smooth
	def aa = _aa
	def padding = _padding
	def spacing = _spacing
	def lineHeight = _lineHeight
	def base = _base
	def scaleW = _scaleW
	def scaleH = _scaleH
	
	override def apply(c: Int) = super.apply(c).asInstanceOf[Option[BitmapFontChar]]
	
	def apply(shape: MutableShape, text: String, kern: Boolean = true, wrapWidth: Double = -1.0, wrapMethod: TextWrapper = WordWrap, textAlignment: HorizontalAlignment = HorizontalAlignment.Center): Seq[RenderedLine] = {
		val lines = wrapWidth match {
			case x if (x <= 0.0) => List(text)
			case _ => wrapMethod(text, wrapWidth, this, kern)
		}
		val vertices = new ArrayBuffer[Vec3]
		val texcoords = new ArrayBuffer[Vec2]
		var yOffset = (lineHeight * (lines.length - 1)) / 2.0
		var renderedLines = new Array[RenderedLine](lines.length)
		for ((line, n) <- lines zipWithIndex) {
			val lineWidth = measureWidth(line, kern)
			var previous: BitmapFontChar = null
			val characters = new Array[RenderedCharacter](line.length)
			val renderedLine = RenderedLine(line, characters, this)
			var xOffset = textAlignment match {
				case HorizontalAlignment.Left => wrapWidth / -2.0
				case HorizontalAlignment.Right => (wrapWidth / 2.0) - lineWidth
				case _ => lineWidth / -2.0
			}
			for ((c, index) <- line zipWithIndex) {
				val fontChar = apply(c) match {
					case Some(fc) => fc
					case None => apply(' ').get
				}
				
				if (kern) xOffset += fontChar.kerning(previous)
				xOffset += fontChar.xAdvance / 2.0
				
				characters(index) = RenderedCharacter(xOffset, yOffset, fontChar, c, renderedLine)
				
				val charYOffset = -fontChar.yOffset + ((lineHeight / 2.0) - (fontChar.quad.height / 2.0))
				
				for (v <- fontChar.quad.vertex.data) {
					val vec = Vec3(v.x + xOffset, v.y + yOffset + charYOffset, v.z)
					vertices += vec
				}
				for (v <- fontChar.quad.texture.data) {
					texcoords += v
				}
				
				xOffset += fontChar.xAdvance / 2.0
				
				previous = fontChar
			}
			yOffset -= lineHeight
			
			renderedLines(n) = renderedLine
		}
		shape.mode = ShapeMode.Quads
		shape.vertex = VertexData(vertices)
		shape.texture = TextureData(texcoords)
		
		renderedLines
	}
	
	def drawString(s: String, kern: Boolean = true) = {
		glCullFace(GL_BACK)
		glEnable(GL_CULL_FACE)
		
		glTranslated(measureWidth(s, kern) / -2.0, 0.0, 0.0)
		
		var previous: BitmapFontChar = null
		for (c <- s) {
			val current = apply(c) match {
				case Some(fc) => fc
				case None => apply(' ').get
			}
			current.drawChar(previous, kern)
			previous = current
		}
	}
	
	def measureWidth(s: String, kern: Boolean = true) = {
		var width = 0.0
		var previous: BitmapFontChar = null
		for (c <- s) {
			val current = apply(c) match {
				case Some(fc) => fc
				case None => apply(' ').get
			}
			width += current.measureCharWidth(previous, kern)
			previous = current
		}
		
		width
	}
	
	def derive(size: Double) = {
		val scale = size / this.size
		
		// Create derived font
		val font = new BitmapFont(texture)
		font._face = face
		font._size = size
		font._bold = bold
		font._italic = italic
		font._charset = charset
		font._unicode = unicode
		font._stretchH = stretchH
		font._smooth = smooth
		font._aa = aa
		font._padding = padding
		font._spacing = spacing
		font._lineHeight = lineHeight
		font._base = base
		font._scaleW = scaleW
		font._scaleH = scaleH
		
		// Scaled characters
		for (bfc <- instances) {
			val fontChar = font.create(bfc.code, bfc._x, bfc._y, bfc._width, bfc._height, scale).asInstanceOf[BitmapFontChar]
			fontChar._font = font
			fontChar._code = bfc.code
			fontChar._xOffset = bfc.xOffset * scale
			fontChar._yOffset = bfc.yOffset * scale
			fontChar._xAdvance = bfc.xAdvance * scale
			
			// Scaled kernings
			for (k <- bfc.kernings) {
				val kerning = FontKerning(k.previous, k.amount * scale)
				fontChar._kernings = kerning :: fontChar._kernings
			}
		}
		
		font
	}
}

object BitmapFont {
	def apply(name: String, imageType: String = "png") = {
		val fnt = Resource(name + ".fnt")
		val png = Resource(name + "." + imageType)
		
		val font = new BitmapFont(TextureManager(png))
		
		val source = Source.fromURL(fnt.url)
		val lines = source.getLines().toList
		
		processInfo(font, lines(0))
		processCommon(font, lines(1))
		
		// Process Characters
		var offset = 2
		for (i <- 0 until font._pages) {
			offset = processPage(font, lines, offset)
		}
		
		font
	}
	
	private def processInfo(font: BitmapFont, s: String) = {
		val m = processLine(s)
		font._face = m("face")
		font._size = m("size").toInt
		font._bold = m("bold").toInt
		font._italic = m("italic").toInt
		if (m.contains("charset")) font._charset = m("charset")
		font._unicode = m("unicode").toInt
		font._stretchH = m("stretchH").toInt
		font._smooth = m("smooth").toInt
		font._aa = m("aa").toInt
		var p = m("padding").split(",")
		font._padding(0) = p(0).toInt
		font._padding(1) = p(1).toInt
		font._padding(2) = p(2).toInt
		font._padding(3) = p(3).toInt
		p = m("spacing").split(",")
		font._spacing(0) = p(0).toInt
		font._spacing(1) = p(1).toInt
	}
	
	private def processCommon(font: BitmapFont, s: String) = {
		val m = processLine(s)
		font._lineHeight = m("lineHeight").toInt
		font._base = m("base").toInt
		font._scaleW = m("scaleW").toInt
		font._scaleH = m("scaleH").toInt
		font._pages = m("pages").toInt
		font._packed = m("packed").toInt
	}
	
	private def processPage(font: BitmapFont, lines: List[String], offset: Int) = {
		var line = offset
		
		// Process "page"
		val pageData = processLine(lines(line))
		val id = pageData("id").toInt
		val file = pageData("file")
		
		line += 1
		
		// Process "chars"
		val charsData = processLine(lines(line))
		val count = charsData("count").toInt

		line += 1
		
		// Process "char"
		for (index <- line until lines.length) {
			val m = processLine(lines(index))
			
			if (m.contains("char")) {
				val id = m("id").toInt
				val x = m("x").toDouble
				val y = m("y").toDouble
				val width = m("width").toDouble - 1.0					// TODO: temporary work-around - displaying too much
				val height = m("height").toDouble - 1.0
				val fontChar = font.create(id, x, y, width, height).asInstanceOf[BitmapFontChar]
				fontChar._font = font
				fontChar._code = id
				fontChar._x = x
				fontChar._y = y
				fontChar._width = width
				fontChar._height = height
				fontChar._xOffset = m("xoffset").toDouble
				fontChar._yOffset = m("yoffset").toDouble
				fontChar._xAdvance = m("xadvance").toDouble
			} else if (m.contains("kernings")) {
				// Ignore
			} else if (m.contains("kerning")) {
				val k = FontKerning(m("first").toInt, m("amount").toInt)
				val next = font(m("second").toInt) match {
					case Some(fc) => fc
					case None => font(' ').get
				}
				next._kernings = k :: next._kernings
			} else {
				throw new RuntimeException("Error parsing: " + lines(index))
			}
		}
		
		line
	}
	
	private def processKernings(font: BitmapFont, lines: List[String], offset: Int, kerningsCount: Int) = {
		for (i <- offset until lines.length) {
			val m = processLine(lines(i))
			
			val k = FontKerning(m("first").toInt, m("amount").toInt)
			val next = font(m("second").toInt) match {
				case Some(fc) => fc
				case None => font(' ').get
			}
			next._kernings = k :: next._kernings
		}
	}
	
	private def processLine(s: String) = {
		var m = Map.empty[String, String]
		
		var quotesOpen = false
		var key: String = null
		var value: String = null
		val buffer = new StringBuilder()
		for (c <- s) {
			if ((quotesOpen) && (c == '"')) {
				quotesOpen = false
			} else if (c == '"') {
				quotesOpen = true
			} else if ((!quotesOpen) && (c == ' ')) {
				value = buffer.toString
				if ((key != null) && (value.length > 0)) {
					m += key -> value
				} else if (value.length > 0) {
					m += value -> "true"
				}
				
				key = null
				value = null
				buffer.delete(0, buffer.length)
			} else if ((!quotesOpen) && (c == '=')) {
				key = buffer.toString
				buffer.delete(0, buffer.length) 
			} else {
				buffer.append(c)
			}
		}
		
		value = buffer.toString
		if ((key != null) && (value.length > 0)) {
			m += key -> value
		}
		
		m
	}
}
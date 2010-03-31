package org.sgine.render

import java.net.URL

import scala.io.Source

class AngelCodeFont private(texture: Texture) extends TextureMap(texture) {
	private var _face: String = null
	private var _size: Int = 0
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
	
	def apply(c: Char): AngelCodeFontChar = apply(c.toString).asInstanceOf[AngelCodeFontChar]
	
	override protected def createImage() = new AngelCodeFontChar()
}

object AngelCodeFont {
	def apply(source: Source, url: URL) = {
		val font = new AngelCodeFont(TextureUtil(url))
		
		val lines = source.getLines().toList
		
		processInfo(font, lines(0))
		processCommon(font, lines(1))
		
		var offset = 2
		for (i <- 0 until font._pages) {
			offset = processPage(font, lines, offset)
		}
		
		font
	}
	
	private def processInfo(font: AngelCodeFont, s: String) = {
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
	
	private def processCommon(font: AngelCodeFont, s: String) = {
		val m = processLine(s)
		font._lineHeight = m("lineHeight").toInt
		font._base = m("base").toInt
		font._scaleW = m("scaleW").toInt
		font._scaleH = m("scaleH").toInt
		font._pages = m("pages").toInt
		font._packed = m("packed").toInt
	}
	
	private def processPage(font: AngelCodeFont, lines: List[String], offset: Int) = {
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
		for (index <- 0 to count) {
			val m = processLine(lines(line))
			val id = m("id").toInt
			val x = m("x").toDouble
			val y = m("y").toDouble
			val width = m("width").toDouble
			val height = m("height").toDouble
			val fontChar = font.create(id.toChar.toString, x, y, width, height).asInstanceOf[AngelCodeFontChar]
			fontChar._xOffset = m("xoffset").toDouble
			fontChar._yOffset = m("yoffset").toDouble
			
			line += 1
		}
		
		line
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
package org.sgine.render.font

import org.sgine.core.Resource

import org.sgine.render.TextureManager

import org.sgine.util.XMLUtil._

import scala.xml._

object BitmapFontLoader {
	def load(fnt: Resource) = {
		// Load XML
		val xml = XML.load(fnt.url)
		
		// Reference main elements
		val info = xml \! "info"
		val common = xml \! "common"
		val pages = xml \! "pages" \ "page"
		if (pages.length != 1) {
			throw new RuntimeException("Multiple paged fonts are currently not supported!")
		}
		val page = pages.head
		val chars = xml \! "chars"
		val kernings = xml \! "kernings"
		
		// Load texture
		val filename = page \@ "file"
		val file = fnt.parent.child(filename)
		val texture = TextureManager(file)
		
		// Create BitmapFont and load info
		val font = new BitmapFont(texture)
		loadInfo(font, info)
		loadCommon(font, common)
		
		// Populate BitmapFontChars
		for (char <- chars \ "char") {
			loadChar(font, char)
		}
		
		// Populate kerning data
		for (kerning <- kernings \ "kerning") {
			loadKerning(font, kerning)
		}
		
		font
	}
	
	private def loadInfo(font: BitmapFont, info: Node) = {
		font._face = info \@ "face"
		if (font.face.indexOf(',') != -1) {
			val split = font.face.indexOf(',')
			val face = font.face.substring(0, split)
			val style = font.face.substring(split + 1)
			font._face = face
			font._style = style match {
				case "Normal" => Font.Plain
				case _ => throw new RuntimeException("Unknown style: " + style)
			}
		}
		
		font._size = (info \@ "size").toInt
		font._bold = (info \@ "bold").toInt
		font._italic = (info \@ "italic").toInt
		font._charset = info \@ "charset"
		font._unicode = (info \@ "unicode").toInt
		font._stretchH = (info \@ "stretchH").toInt
		font._smooth = (info \@ "smooth").toInt
		font._aa = (info \@ "aa").toInt
		
		val p = (info \@ "padding").split(",")
		font._padding(0) = p(0).toInt
		font._padding(1) = p(1).toInt
		font._padding(2) = p(2).toInt
		font._padding(3) = p(3).toInt
		
		val s = (info \@ "spacing").split(",")
		font._spacing(0) = p(0).toInt
		font._spacing(1) = p(1).toInt
	}
	
	private def loadCommon(font: BitmapFont, common: Node) = {
		font._lineHeight = (common \@ "lineHeight").toInt
		font._base = (common \@ "base").toInt
		font._scaleW = (common \@ "scaleW").toInt
		font._scaleH = (common \@ "scaleH").toInt
		font._pages = (common \@ "pages").toInt
		font._packed = (common \@ "packed").toInt
	}
	
	private def loadChar(font: BitmapFont, char: Node) = {
		val id = (char \@ "id").toInt
		val x = (char \@ "x").toInt
		val y = (char \@ "y").toInt
		val width = (char \@ "width").toInt
		val height = (char \@ "height").toInt
		val fontChar = font.create(id, x, y, width, height).asInstanceOf[BitmapFontChar]
		fontChar._font = font
		fontChar._code = id
		fontChar._x = x
		fontChar._y = y
		fontChar._width = width
		fontChar._height = height
		fontChar._xOffset = (char \@ "xoffset").toDouble
		fontChar._yOffset = (char \@ "yoffset").toDouble
		fontChar._xAdvance = (char \@ "xadvance").toDouble
	}
	
	private def loadKerning(font: BitmapFont, kerning: Node) = {
		val first = (kerning \@ "first").toInt
		val second = (kerning \@ "second").toInt
		val amount = (kerning \@ "amount").toInt
		val k = FontKerning(first, amount)
		val next = font(second) match {
			case Some(fc) => fc
			case None => font(' ').get
		}
		next._kernings = k :: next._kernings
	}
}
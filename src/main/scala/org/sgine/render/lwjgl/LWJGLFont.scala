package org.sgine.render.lwjgl

import scala.io.Source

class LWJGLFont {
}

case class LWJGLFontChar(id: Int, x: Int, y: Int, width: Int, height: Int, xOffset: Int, yOffset: Int, xAdvance: Int, page: Int, chnl: Int)

object LWJGLFont {
	def apply(source: Source) = {
		val list = source.getLines().toList
		
		var face = ""
		var size = 0
		var bold = 0
		var italic = 0
		var charset = ""
		var unicode = 0
		var stretchH = 0
		var smooth = 0
		var aa = 0
		var paddingTop = 0
		var paddingBottom = 0
		var paddingLeft = 0
		var paddingRight = 0
		var spacing1 = 0
		var spacing2 = 0
		var lineHeight = 0
		var base = 0
		var scaleW = 0
		var scaleH = 0
		var pages = 0
		var packed = 0
		
		val info = list(0)
		println(info)
	}
}
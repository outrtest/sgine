package org.sgine.render

import org.lwjgl.opengl.GL11._

class AngelCodeFontChar extends Image {
	protected[render] var _font: AngelCodeFont = _
	protected[render] var _code: Int = _
	protected[render] var _xOffset: Double = _
	protected[render] var _yOffset: Double = _
	protected[render] var _xAdvance: Double = _
	protected[render] var _kernings: List[AngelCodeFontKerning] = Nil

	def font = _font
	def code = _code
	def xOffset = _xOffset
	def yOffset = _yOffset
	def xAdvance = _xAdvance
	def kernings = _kernings
	
	def drawChar(previous: AngelCodeFontChar = null, kerning: Boolean = true) = {
		val adjust = if (previous != null) previous.xAdvance / 2.0 else 0.0
		glTranslated(adjust + (xAdvance / 2.0), 0.0, 0.0)
//		glTranslated(xAdvance / 2.0, 0.0, 0.0)
		draw(xOffset, -yOffset / 2.0)
//		glTranslated(xAdvance / 2.0, 0.0, 0.0)
	}
}
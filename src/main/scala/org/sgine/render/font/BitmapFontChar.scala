package org.sgine.render.font

import org.sgine.render.shape.Quad

import org.lwjgl.opengl.GL11._

class BitmapFontChar protected(quad: Quad) extends FontChar {
	protected[render] var _font: BitmapFont = _
	protected[render] var _code: Int = _
	protected[render] var _xOffset: Double = _
	protected[render] var _yOffset: Double = _
	protected[render] var _xAdvance: Double = _
	protected[render] var _kernings: List[FontKerning] = Nil

	def font = _font
	def code = _code
	def xOffset = _xOffset
	def yOffset = _yOffset
	def xAdvance = _xAdvance
	def kernings = _kernings
	
	def kerning(previous: FontChar): Double = {
		if (previous != null) {
			kernings.find(k => k.previous == previous.code) match {
				case Some(k) => k.amount
				case _ => 0.0
			}
		} else {
			0.0
		}
	}
	
	def drawChar(previous: FontChar = null, kern: Boolean = true) = {
		val k = if (kern) kerning(previous) else 0.0
		
		// Adjust before we draw
		val adjust = if (previous != null) previous.xAdvance / 2.0 else 0.0
		val ox = xOffset
		val oy = -yOffset + ((font.lineHeight / 2.0) - (quad.height / 2.0))
		glTranslated(adjust + (xAdvance / 2.0) + k + ox, oy, 0.0)
		
		// Draw character to screen
//		draw(xOffset, -yOffset + ((font.lineHeight / 2.0) - (height / 2.0)))
		font.texture.bind()
		quad()
		
		glTranslated(-ox, -oy, 0.0)
	}
	
	def measureCharWidth(previous: FontChar = null, kern: Boolean = true) = {
		val k = if (kern) kerning(previous) else 0.0
		
		xAdvance + k
	}
	
	override def toString() = code + " (" + code.toChar + ") Offset: " + xOffset + "x" + yOffset + " xAdvance: " + xAdvance
}
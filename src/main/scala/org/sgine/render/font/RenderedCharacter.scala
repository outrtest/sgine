package org.sgine.render.font

class RenderedCharacter(val x: Double, val y: Double, val fontChar: FontChar, val char: Char, protected[font] var _line: RenderedLine) {
	def line = _line
	
	override def toString() = "RenderedCharacter(" + x + ", " + y + ", " + fontChar + ")"
}

object RenderedCharacter {
	def apply(x: Double, y: Double, fontChar: FontChar, char: Char, line: RenderedLine) = new RenderedCharacter(x, y, fontChar, char, line)
}
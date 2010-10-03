package org.sgine.render.font

case class RenderedCharacter(x: Double, y: Double, char: FontChar, line: RenderedLine) {
	override def toString() = "RenderedCharacter(" + x + ", " + y + ", " + char + ")"
}
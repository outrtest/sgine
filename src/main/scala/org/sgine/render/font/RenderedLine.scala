package org.sgine.render.font

class RenderedLine(val text: String, val characters: Seq[RenderedCharacter], val font: Font) {
	override def toString() = "RenderedLine(" + text + ")"
}

object RenderedLine {
	def apply(text: String, characters: Seq[RenderedCharacter], font: Font) = new RenderedLine(text, characters, font)
}
package org.sgine.visual.material.pigment

import org.sgine.visual._

trait ColoredPigment {
	val color: Color
}

object ColoredPigment {
	def apply(color: Color) = new ColoredPigmentImpl(color)
}

class ColoredPigmentImpl(val color: Color) extends ColoredPigment
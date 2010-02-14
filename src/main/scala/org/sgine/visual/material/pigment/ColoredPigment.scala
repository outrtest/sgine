package org.sgine.visual.material.pigment

import org.sgine.core.Color
import org.sgine.visual._
import org.sgine.visual.material._

trait ColoredPigment extends Pigment {
	val color: Color
}

object ColoredPigment {
	def apply(color: Color) = new ColoredPigmentImpl(color)
}

class ColoredPigmentImpl(val color: Color) extends ColoredPigment
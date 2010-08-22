package org.sgine.render.shape

import org.sgine.core.Color

case class ColorData(data: Seq[Color]) {
	def apply(index: Int) = data(index)
}
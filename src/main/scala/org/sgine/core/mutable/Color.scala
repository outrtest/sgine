package org.sgine.core.mutable

import org.sgine.core.{Color => ImmutableColor}

class Color extends ImmutableColor {
	def red_=(red: Double) = _red = red
	def green_=(green: Double) = _green = green
	def blue_=(blue: Double) = _blue = blue
	def alpha_=(alpha: Double) = _alpha = alpha
	
	def apply(color: Color) = {
		red = color.red
		green = color.green
		blue = color.blue
		alpha = color.alpha
	}
}
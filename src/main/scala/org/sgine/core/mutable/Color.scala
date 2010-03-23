package org.sgine.core.mutable

import org.sgine.core.{Color => ImmutableColor}

class Color extends ImmutableColor {
	def red_=(red: Float) = _red = red
	def green_=(green: Float) = _green = green
	def blue_=(blue: Float) = _blue = blue
	def alpha_=(alpha: Float) = _alpha = alpha
	
	def apply(color: Color) = {
		red = color.red
		green = color.green
		blue = color.blue
		alpha = color.alpha
	}
}
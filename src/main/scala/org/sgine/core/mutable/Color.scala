package org.sgine.core.mutable

import org.sgine.core.Modifiable
import org.sgine.core.{Color => ImmutableColor}

class Color private() extends ImmutableColor with Modifiable {
	def red_=(red: Double) = {
		_red = red
		modified()
	}
	def green_=(green: Double) = {
		_green = green
		modified()
	}
	def blue_=(blue: Double) = {
		_blue = blue
		modified()
	}
	def alpha_=(alpha: Double) = {
		_alpha = alpha
		modified()
	}
	
	def apply(color: ImmutableColor) = {
		_red = color.red
		_green = color.green
		_blue = color.blue
		_alpha = color.alpha
		modified()
	}
}

object Color {
	def apply() = new Color()
	
	def apply(color: ImmutableColor) = {
		val c = new Color()
		c(color)
		c
	}
}
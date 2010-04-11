package org.sgine.effect

import org.sgine.property.AdjustableProperty

class PropertySetEffect[T](val property: AdjustableProperty[T], val value: T) extends Effect {
	def play() = property.set(value)
	
	def finished = true
}
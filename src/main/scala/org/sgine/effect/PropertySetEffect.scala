package org.sgine.effect

import org.sgine.property.AnimatingProperty

class PropertySetEffect[T](val property: AnimatingProperty[T], val value: T) extends Effect {
	def play() = property.set(value)
	
	def finished = true
}
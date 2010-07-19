package org.sgine.effect

import org.sgine.event.EventHandler

import org.sgine.property.Property
import org.sgine.property.event.PropertyChangeEvent

class PropertyChangeEffect[T](val property: Property[T], val value: T) extends Effect {
	def play() = property := value
	
	def finished = property() == value
}
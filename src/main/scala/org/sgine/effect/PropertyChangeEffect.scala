package org.sgine.effect

import org.sgine.event.EventHandler

import org.sgine.property.ListenableProperty
import org.sgine.property.event.PropertyChangeEvent

class PropertyChangeEffect[T](val property: ListenableProperty[T], val value: T) extends Effect {
	def play() = property := value
	
	def finished = property() == value
}
package org.sgine.property.event

import org.sgine.event._

import org.sgine.path.PathElementChangeEvent

import org.sgine.property._

class PropertyChangeEvent[T] protected(val property: Property[T] with Listenable, oldValue: T, newValue: T, adjusting: Boolean) extends ChangeEvent(property, oldValue, newValue, adjusting) {
	override def retarget(target: org.sgine.event.Listenable): PropertyChangeEvent[T] = PropertyChangeEvent(target.asInstanceOf[ListenableProperty[T]], oldValue, newValue, adjusting)
	
	override def toString() = "PropertyChangeEvent(" + property + ": " + oldValue + " - " + newValue + ")"
}

object PropertyChangeEvent {
	def apply[T](property: Property[T] with Listenable, oldValue: T, newValue: T, adjusting: Boolean) = {
		new PropertyChangeEvent(property, oldValue, newValue, adjusting)
	}
}
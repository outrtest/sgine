package org.sgine.property

import org.sgine.event._

class PropertyChangeEvent[T](val property: ListenableProperty[T], val oldValue: T, val newValue: T, val adjusting: Boolean) extends Event(property) {
	processNormal = !adjusting
}
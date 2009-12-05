package org.sgine.property

import org.sgine.event._

class PropertyChangeEvent[T](val property: ListenableProperty[T], val oldValue: T, val newValue: T) extends Event(property)
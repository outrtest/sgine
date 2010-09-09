package org.sgine.property.event

class PropertyChangeDelegate[O] private(val delegate: Function1[O, Unit]) extends Function1[PropertyChangeEvent[O], Unit] {
	def apply(evt: PropertyChangeEvent[O]) = {
		delegate(evt.newValue)
	}
}

object PropertyChangeDelegate {
	def apply[O](delegate: (O) => Unit) = new PropertyChangeDelegate[O](delegate)
}
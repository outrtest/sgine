package org.sgine.property.state

trait Stateful {
	val states = new StateManager(this)
}
package org.sgine.property.state

import org.sgine.event.Listenable

trait Stateful extends Listenable {
	val states = new StateManager(this)
}
package org.sgine.property.state

import org.sgine.event.Listenable

trait Stateful extends Listenable {
	val states = new StateManager(this)
	
	/**
	 * Make sure all states are updated properly. This may
	 * be useful to call after initialization of functionality
	 * that may not have been available when the state was
	 * initially activated.
	 */
	def updateState() = {
		for (state <- states.active) {
			for (item <- state.items) {
				states.activateItem(item)
			}
		}
	}
}
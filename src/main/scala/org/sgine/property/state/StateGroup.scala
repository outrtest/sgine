package org.sgine.property.state

class StateGroup private[state](val manager: StateManager, val name: String) {
	protected[state] var states: List[State] = Nil
	
	protected[state] def create(name: String) = synchronized {
		val state = new State(this, name)
		states = state :: states
		state
	}
	
	def apply(name: String) = states.find(_.name == name)
}
package org.sgine.property.state

trait Stateful {
	val states = new StateManager(this)
}

class StateManager(o: Any) {
	private var map = Map.empty[String, StateInstance]
	
	def active = map.values.collect{case instance if (instance.active) => instance.state}
	
	def inactive = map.values.collect{case instance if (!instance.active) => instance.state}
	
	def all = map.values.map(_.state)
	
	def activate(name: String): Boolean = {
		synchronized {
			val instance = map(name)
			
			if (!instance.active) {
				instance.originals = instance.state.activate(o)
				instance.active = true
				
				true
			} else {			// Already active
				false
			}
		}
	}
	
	def deactivate(name: String): Boolean = {
		synchronized {
			val instance = map(name)
			
			if (instance.active) {
				instance.state.deactivate(o, instance.originals)
				instance.active = false
				
				true
			} else {			// Not active
				false
			}
		}
	}
	
	def activate(state: State): Boolean = {
		synchronized {
			if (!map.contains(state.name)) {
				map += state.name -> new StateInstance(state, false, Nil)
			}
		}
		activate(state.name)
	}
	
	def deactivate(state: State): Boolean = {
		deactivate(state.name)
	}
	
	def +=(state: State) = {
		synchronized {
			if (!map.contains(state.name)) {
				map += state.name -> new StateInstance(state, false, Nil)
				
				true
			} else {
				false
			}
		}
	}
	
	def -=(state: State) = {
		synchronized {
			deactivate(state)
			map -= state.name
		}
	}
	
	def deactivateAll() = {
		for (s <- active) {
			deactivate(s)
		}
	}
	
	def clear() = {
		synchronized {
			deactivateAll()
			map = Map.empty
		}
	}
}

class StateInstance(val state: State, var active: Boolean, var originals: List[Any])
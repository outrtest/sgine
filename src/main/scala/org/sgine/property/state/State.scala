package org.sgine.property.state

import org.sgine.path.PathSupport

class State private[state](val manager: StateManager, val name: String) {
	protected[state] var items: List[StateItem] = Nil
	
	def update(path: String, value: Any) = synchronized {
		items = items.filterNot(_.path == path)		// Remove existing path if found
		items = StateItem(path, value) :: items		// Add new StateItem
	}
	
	def activate() = manager.activate(this)
	
	def deactivate() = manager.deactivate(this)
	
	def active = manager.isActive(this)
	
	def hasPath(path: String) = items.find(_.path == path) != None
}

case class StateItem(path: String, value: Any)
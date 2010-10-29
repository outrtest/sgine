package org.sgine.property.state

class State private[state](val group: StateGroup, val name: String) {
	protected[state] var items: List[StateItem] = Nil
	
	def update(path: String, value: Any) = synchronized {
		items = items.filterNot(_.path == path)		// Remove existing path if found
		items = StateItem(path, value) :: items		// Add new StateItem
	}
	
	def activate() = group.manager.activate(this)
	
	def active = group.manager.isActive(this)
	
	def hasPath(path: String) = items.find(_.path == path) != None
}

case class StateItem(path: String, value: Any)
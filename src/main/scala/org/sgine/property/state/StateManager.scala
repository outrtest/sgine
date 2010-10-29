package org.sgine.property.state

import org.sgine.path.OPath

import org.sgine.property.Property

class StateManager private[state](val stateful: Stateful) {
	private var groups: List[StateGroup] = Nil
	
	private var originals = Map.empty[String, Any]
	private var active: List[State] = Nil
	
	def create(group: String, name: String) = {
		groups.view.flatMap(_.states).find(_.name == name) match {
			case Some(s) => throw new RuntimeException("Cannot create an already existing state: " + name)
			case None => {
				val g = this.group(group)
				val s = g.create(name)
				
				s
			}
		}
	}
	
	def isActive(state: State) = active.indexOf(state) != -1
	
	def apply(name: String) = groups.view.flatMap(_.states).find(_.name == name).getOrElse(throw new NullPointerException("Cannot find state: " + name))
	
	def group(group: String): StateGroup = {
		groups.find(_.name == group) match {
			case Some(g) => g
			case None => synchronized {
				val g = new StateGroup(this, group)
				groups = g :: groups
				g
			}
		}
	}
	
	
	
	def activate(name: String): Unit = activate(apply(name))
	
	def activate(state: State): Unit = synchronized {
		if (!state.active) {
			for (item <- state.items) {
				OPath.resolve(stateful, item.path) match {
					case Some(o) => o match {
						case p: Property[_] => {
							registerOriginal(item.path, p())		// Have a value to return to
							p.erasured(item.value)					// Assign the value to the property
						}
						case _ => throw new RuntimeException("Unsupported end-point: " + o.asInstanceOf[AnyRef].getClass.getName)
						
					}
					case None => // Unable to resolve
				}
			}
			active = state :: active
		}
	}
	
	def deactivate(name: String): Unit = deactivate(apply(name))
	
	def deactivate(state: State): Unit = synchronized {
		if (state.active) {
			for (item <- state.items) {
				val option = restoreOriginal(item.path, state) match {
					case Some(original) => {
						OPath.resolve(stateful, item.path) match {
							case Some(o) => o match {
								case p: Property[_] => {
									p.erasured(original)
								}
								case _ => throw new RuntimeException("Unsupported end-point: " + o.asInstanceOf[AnyRef].getClass.getName)
								
							}
							case None => // Unable to resolve
						}
					}
					case None => // No change necessary
				}
			}
			active = active.filterNot(_ == state)
		}
	}
	
	private def registerOriginal(path: String, value: Any) = {
		originals.get(path) match {
			case Some(v) => // Already have original registered
			case None => originals += path -> value
		}
	}
	
	private def restoreOriginal(path: String, state: State) = {
		val matches = for (s <- active; if (s.hasPath(path))) yield s
		if (state == matches.head) {		// Top of the stack - currently active
			if (matches.length > 1) {		// Activate the next one back
				Some(matches(1).items.find(_.path == path).get.value)
			} else {						// Only one, go back to original
				val original = originals(path)
				originals -= path
				Some(original)
			}
		} else {							// It's not currently active, no change necessary
			None
		}
	}
}
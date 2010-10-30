package org.sgine.property.state

import org.sgine.path.OPath
import org.sgine.path.PathSupport

import org.sgine.property.Property
import org.sgine.property.container.PropertyContainer

class StateManager private[state](val stateful: Stateful) extends PropertyContainer with PathSupport {
	override val parent = stateful
	
	private var states = Map.empty[String, State]
	
	private var originals = Map.empty[String, Any]
	private var active: List[State] = Nil
	
	private def create(name: String) = synchronized {
		states.get(name) match {
			case Some(s) => s
			case None => {
				val state = new State(this, name)
				states += name -> state
				
				state
			}
		}
	}
	
	def isActive(state: State) = active.indexOf(state) != -1
	
	def apply(name: String) = states.getOrElse(name, create(name))
	
	def option(name: String) = states.get(name)
	
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
				originals.get(path) match {
					case Some(original) => {
						originals -= path
						Some(original)
					}
					case None => None
				}
			}
		} else {							// It's not currently active, no change necessary
			None
		}
	}
	
	override def resolveElement(key: String) = Some(apply(key))
}
package org.sgine.property.state

import org.sgine.property.Property
import org.sgine.property.container.PropertyContainer

class State(val name: String) {
	private var items: List[StateItem] = Nil
	
	def add(key: String, value: Any, restore: Any = null) = {
		synchronized {
			items = StateItem(key, value, null) :: items
		}
	}
	
	def activate(o: Any) = {
		var originals: List[Any] = Nil
		for (i <- items) {
			originals = i.activate(o) :: originals
		}
		
		originals.reverse
	}
	
	def deactivate(o: Any, originals: List[Any]) = {
		for ((i, original) <- items.zip(originals)) {
			i.deactivate(o, original)
		}
	}
}

case class StateItem(key: String, value: Any, restore: Any = null) {
	def activate(o: Any) = {
		val original = process(o, key, value)
		if (restore != null) {
			restore
		} else {
			original
		}
	}
	
	def deactivate(o: Any, original: Any) = {
		process(o, key, original)
	}
	
	private def process[T](o: Any, key: String, value: T): Any = {
		// Process first part of key
		var name = key
		var tail: String = null
		if ((name != null) && (name.indexOf('.') != -1)) {
			name = name.substring(0, name.indexOf('.'))
			tail = key.substring(key.indexOf('.') + 1)
		}
		
		// Find out what we're processing here
		o match {
			case pc: PropertyContainer => {
				val next = pc(name)
				if (next == None) {
					throw new NullPointerException("Unable to find property named: " + name + " in PropertyContainer: " + o)
				}
				process(next.get, tail, value)
			}
			case p: Property[_] => {
				if (tail != null) {
					process(p(), tail, value)
				} else {
					val original = p()
					p.asInstanceOf[Property[T]] := value
					
					original
				}
			}
			case _ => throw new RuntimeException("Unable to find match: " + o + ", " + key + ": " + value)
		}
	}
}
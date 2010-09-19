package org.sgine.property.state

import org.sgine.path.OPath

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
		OPath.resolve(o.asInstanceOf[AnyRef], key) match {
			case Some(m) => m match {
				case p: Property[_] => {
					val original = p()
					p.asInstanceOf[Property[T]] := value
					original
				}
				case _ => throw new RuntimeException("Unsupported end-point: " + m.asInstanceOf[AnyRef].getClass.getName)
			}
			case None => throw new RuntimeException("Couldn't find: " + key + " in " + o + " with value: " + value)
		}
	}
}
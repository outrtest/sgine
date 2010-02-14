package org.sgine.property

import java.util.concurrent.ArrayBlockingQueue
import org.sgine.event._

class PropertyChangeEvent[T] private (var property: ListenableProperty[T], var oldValue: T, var newValue: T, var adjusting: Boolean) extends Event(property)

object PropertyChangeEvent {		// TODO: test recycling
//	private val cache = new ArrayBlockingQueue[PropertyChangeEvent[_]](1000)
	
	def apply[T](property: ListenableProperty[T], oldValue: T, newValue: T, adjusting: Boolean) = {
//		var e = cache.poll()
//		if (e == null) {
			var e = new PropertyChangeEvent[T](property, oldValue, newValue, adjusting)
//			println("created new property")
//		} else {
//			println("reusing!")
//		}
		e
	}
	
	def release(e: PropertyChangeEvent[_]) = {
//		cache.add(e)
//		println("added to cache!")
	}
}
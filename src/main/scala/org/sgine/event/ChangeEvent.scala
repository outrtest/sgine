package org.sgine.event

import org.sgine.path.PathElementChangeEvent

class ChangeEvent[T] protected(listenable: Listenable, val oldValue: T, val newValue: T, val adjusting: Boolean) extends Event(listenable) with PathElementChangeEvent {
	def retarget(target: Listenable): Event = ChangeEvent(target, oldValue, newValue, adjusting)
	
	override def toString() = "ChangeEvent(" + listenable + ": " + oldValue + " - " + newValue + ")"
}

object ChangeEvent {
	def apply[T](listenable: Listenable, oldValue: T, newValue: T, adjusting: Boolean) = {
		new ChangeEvent(listenable, oldValue, newValue, adjusting)
	}
}
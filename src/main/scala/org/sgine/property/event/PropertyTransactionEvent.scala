package org.sgine.property.event

import org.sgine.event._
import org.sgine.property._

class PropertyTransactionEvent[T]  private (val property: ListenableProperty[T], val oldValue: T, val newValue: T, commit: Boolean) extends Event(property) {
	def isCommit = commit
	
	def isRevert = !commit
	
	def retarget(target: org.sgine.event.Listenable): Event = PropertyTransactionEvent(target.asInstanceOf[ListenableProperty[T]], oldValue, newValue, commit)
}

object PropertyTransactionEvent {
	def apply[T](property: ListenableProperty[T], oldValue: T, newValue: T, commit: Boolean) = {
		new PropertyTransactionEvent[T](property, oldValue, newValue, commit)
	}
}
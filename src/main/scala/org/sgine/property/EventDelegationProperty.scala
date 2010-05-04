package org.sgine.property

import org.sgine.event._

trait EventDelegationProperty[T] extends ChangeableProperty[T] with Listenable {
	private val handler = EventHandler(EventDelegate(this), processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	abstract override def changed(oldValue:T, newValue:T):Unit = {
		super.changed(oldValue, newValue)
		
		oldValue match {
			case l: Listenable => l.listeners -= handler
			case _ =>
		}
		
		newValue match {
			case l: Listenable => l.listeners += handler
			case _ =>
		}
	}
}
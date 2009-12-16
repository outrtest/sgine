package org.sgine.event

import org.sgine.work._
import org.sgine.work.unit._

class Event (val listenable: Listenable) {
	/**
	 * Enables or disables recursion to be passed to children upon invocation
	 * of this event.
	 *
	 * Defaults to true
	 */
	protected[event] var recursionParents = true
	/**
	 * Enables or disables recursion to be passed to parents upon invocation
	 * of this event.
	 * 
	 * Defaults to true
	 */
	protected[event] var recursionChildren = true
	/**
	 * Enables or disables ProcessingMode.Normal for this event.
	 * 
	 * Defaults to true
	 */
	protected[event] var processNormal = true
	/**
	 * Enables or disables ProcessingMode.Blocking for this event.
	 * 
	 * Defaults to true
	 */
	protected[event] var processBlocking = true
	/**
	 * Enables or disables ProcessingMode.Asynchronous for this event.
	 * 
	 * Defaults to true
	 */
	protected[event] var processAsynchronous = true
}

object Event {
	var workManager = DefaultWorkManager
	
	def enqueue(evt: Event) = {
		// Pre-Process Event on Listenable
		evt.listenable.processEvent(evt)
		
		// Process Event on blocking handlers
		if (evt.processBlocking) evt.listenable.listeners.filter(_.processingMode == ProcessingMode.Blocking).foreach(_.process(evt))
		
		// Walk up the hierarchy for Recursion.Parents
		processParentRecursion(evt.listenable.parent, evt)
		
		// Walk down the hierarchy for Recursion.Children
		processChildrenRecursion(evt.listenable, evt)
		
		// Enqueue Normal-blocking event
		if (evt.processNormal) workManager += NormalEventWorkUnit(evt)
		
		// Iterate over and enqueue asynchronous entries
		if (evt.processAsynchronous) evt.listenable.listeners.filter(_.processingMode == ProcessingMode.Asynchronous).foreach(workManager += AsynchronousWorkUnit(_, evt))
	}
	
	private def processParentRecursion(l: Listenable, evt: Event): Unit = {
		if ((evt.recursionParents) && (l != null)) {
			l.processChildEvent(evt)
			l.listeners.filter(parentRecursion).filter(_.processingMode == ProcessingMode.Blocking).foreach(_.process(evt))
			
			processParentRecursion(l.parent, evt)
		}
	}
	
	private def processChildrenRecursion(l: AnyRef, evt: Event): Unit = {
		if (evt.recursionChildren) {
			l match {
				case i: Iterable[_] => {
					for (child <- i) child match {
						case lc: Listenable => {
							lc.processParentEvent(evt)
							lc.listeners.filter(childrenRecursion).filter(_.processingMode == ProcessingMode.Blocking).foreach(_.process(evt))
						}
						case _ =>
					}
					for (child <- i) child match {
						case c: Iterable[_] => processChildrenRecursion(c, evt)
						case _ =>
					}
				}
				case _ =>
			}
		}
	}
	
	private def parentRecursion(l: EventHandler) = {
		if (l.recursion == Recursion.Children) {
			true
		} else if (l.recursion == Recursion.All) {
			true
		} else {
			false
		}
	}
	
	private def childrenRecursion(l: EventHandler) = {
		if (l.recursion == Recursion.Parents) {
			true
		} else if (l.recursion == Recursion.All) {
			true
		} else {
			false
		}
	}
}

case class AsynchronousWorkUnit(h: EventHandler, evt: Event) extends Function0[Unit] {
	def apply() = {
		h.process(evt)
	}
}

case class NormalEventWorkUnit(evt: Event) extends BlockingWorkUnit(evt.listenable) {
	def apply() = {
		evt.listenable.listeners.filter(_.processingMode == ProcessingMode.Normal).foreach(_.process(evt));
	}
}
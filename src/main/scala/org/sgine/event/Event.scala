package org.sgine.event

import org.sgine.core.ProcessingMode

import org.sgine.util.Cacheable
import org.sgine.util.ObjectCache

import org.sgine.work._
import org.sgine.work.unit._

abstract class Event(_listenable: Listenable) {
	val cause = Event.current.get
	def listenable = _listenable
	
	protected[event] var counter = new java.util.concurrent.atomic.AtomicInteger(0)			// Keep track of current listener count currently being invoked
	
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
	
	def retarget(target: Listenable): Event
}

object Event {
	val current = new ThreadLocal[Event]()
	var workManager = DefaultWorkManager
	
	def enqueue(evt: Event, target: Listenable = null) = {
		val listenable = if (target != null) target else evt.listenable
		
		val previousCause = current.get()	// Store reference to previous event
		current.set(evt)					// Set current to this
		try {
			// Pre-Process Event on Listenable
			listenable.processEvent(evt)
			
			// Process Event on blocking handlers
			if (evt.processBlocking) {
				for (h <- listenable.listeners) {
					if (isBlocking(h)) {
						h.process(evt)
					}
				}
			}
			
			// Walk up the hierarchy for Recursion.Parents
			processParentRecursion(listenable.parent, evt)
			
			// Walk down the hierarchy for Recursion.Children
			processChildrenRecursion(listenable, evt)
		} finally {
			current.set(previousCause)		// Revert current event back
		}
		
		// Enqueue Normal-blocking event
		if (evt.processNormal) workManager += NormalEventWorkUnit(evt, listenable)
		
		// Iterate over and enqueue asynchronous entries
		if (evt.processAsynchronous) {
			for (h <- listenable.listeners) {
				if (isAsynchronous(h)) {
					workManager += AsynchronousWorkUnit(h, evt)
				}
			}
		}
	}
	
	private val isBlocking = (h: EventHandler) => h.processingMode == ProcessingMode.Blocking
	
	private val isAsynchronous = (h: EventHandler) => h.processingMode == ProcessingMode.Asynchronous
	
	private def processParentRecursion(l: Listenable, evt: Event): Unit = {
		if ((evt.recursionParents) && (l != null)) {
			l.processChildEvent(evt)
			
			for (h <- l.listeners) {
				if (parentRecursion(h)) {
					if (isBlocking(h)) {
						h.process(evt)
					}
				}
			}
			
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
							for (h <- lc.listeners) {
								if (childrenRecursion(h)) {
									if (isBlocking(h)) {
										h.process(evt)
									}
								}
							}
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
	evt.counter.incrementAndGet
	
	def apply() = {
		val previousCause = Event.current.get()
		Event.current.set(evt)
		try {
			h.process(evt)
		} finally {
			Event.current.set(previousCause)
		}
		
		if (evt.counter.decrementAndGet == 0) {
			evt match {
				case c: Cacheable[_] => {
					val ce = c.asInstanceOf[Event with Cacheable[Event]]
					ce.cache.release(ce)
				}
				case _ =>
			}
		}
	}
}

case class NormalEventWorkUnit(evt: Event, listenable: Listenable) extends BlockingWorkUnit(listenable) {
	evt.counter.incrementAndGet
	
	def apply() = {
		val previousCause = Event.current.get()
		Event.current.set(evt)
		try {
			for (h <- listenable.listeners) {
				if (h.processingMode == ProcessingMode.Normal) {
					h.process(evt)
				}
			}
		} finally {
			Event.current.set(previousCause)
		}
		
		if (evt.counter.decrementAndGet == 0) {
			evt match {
				case c: Cacheable[_] => {
					val ce = c.asInstanceOf[Event with Cacheable[Event]]
					ce.cache.release(ce)
				}
				case _ =>
			}
		}
	}
}
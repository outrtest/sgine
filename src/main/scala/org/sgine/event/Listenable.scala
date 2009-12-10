package org.sgine.event

trait Listenable {
	def parent: Listenable
	val listeners = new EventProcessor(this)
	
	/**
	 * Invoked when an event occurs on this listenable.
	 * 
	 * @param evt
	 */
	def processEvent(evt: Event): Unit = {
	}
	
	/**
	 * Invoked when a child listenable of this listenable
	 * receives an event.
	 * 
	 * @param evt
	 */
	def processChildEvent(evt: Event): Unit = {
	}
	
	/**
	 * Invoked when a parent listenable of this listenable
	 * receives an event.
	 * 
	 * @param evt
	 */
	def processParentEvent(evt: Event): Unit = {
	}
}
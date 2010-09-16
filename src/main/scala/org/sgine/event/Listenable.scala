package org.sgine.event

import org.sgine.core.ProcessingMode

import org.sgine.util.Time

import scala.reflect.Manifest

trait Listenable {
	def parent: Listenable = null
	
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
	
	/**
	 * Waits the specified amount of <code>time</code> in seconds for the specified
	 * event to occur. The response will represent the event received or null if an
	 * event was not captured in the time allotted.
	 * 
	 * @param time
	 * @param processingMode
	 * @param recursion
	 * @param filter
	 * @param manifest
	 * @return
	 * 		the event or null if waiting timed-out
	 */
	def waitForEvent[E <: Event](time: Double = 0.0, processingMode: ProcessingMode = ProcessingMode.Normal, recursion: Recursion = Recursion.None, filter: E => Boolean = null)(implicit manifest: Manifest[E]) = {
		var event: E = null.asInstanceOf[E]
		val handler = EventHandler(new Function1[E, Unit] {
			def apply(evt: E) = {
				event = evt
			}
		}, processingMode, recursion, filter)
		listeners += handler
		try {
			Time.waitFor(time) {
				event != null
			}
		} finally {
			listeners -= handler
		}
		event
	}
}
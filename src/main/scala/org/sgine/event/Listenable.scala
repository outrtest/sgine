package org.sgine.event

import java.util.concurrent.atomic.AtomicReference

import org.sgine.bus.ObjectBus

import org.sgine.core.ProcessingMode

import org.sgine.util.Time

import scala.reflect.Manifest

/**
 * Listenable must be mixed into any class that fires events.
 * Simply mixing in this trait gives complete support for events
 * in the class. For hierarchical propagation the <code>parent</code>
 * method should be overrided to reference the parent Listenable
 * associated with this class.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
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
	
	/**
	 * Convenience method to easily listen for a specific event without having to create
	 * a separate method to receive the event and an EventHandler to wrap it.
	 * 
	 * @param processingMode
	 * 		Defaults to Normal
	 * @param recursion
	 * 		Defaults to None
	 * @param filter
	 * 		Defaults to null
	 * @param container
	 * 		If this value is provided the reference is set to the current event
	 * 		being processed. Default value is null.
	 * @param action
	 * 		This is the action that should be taken upon event.
	 * @param manifest
	 * 		Implicit manifest.
	 * @return
	 * 		EventHandler generated and added
	 */
	def onEvent[E <: Event](processingMode: ProcessingMode = ProcessingMode.Normal, recursion: Recursion = Recursion.None, filter: E => Boolean = null, container: AtomicReference[E] = null)(action: => Unit)(implicit manifest: Manifest[E]) = {
		val handler = EventHandler(new Function1[E, Unit] {
			def apply(evt: E) = {
				if (container != null) {
					container.set(evt)
				}
				action
			}
		}, processingMode, recursion, filter)
		listeners += handler
		
		handler
	}
	
	/**
	 * Convenience method to easily propagate events through an ObjectBus. This
	 * method takes an ObjectBus and details about how processing should occur
	 * and returns the associated EventHandler for later removal.
	 * 
	 * @param objectBus
	 * 			The ObjectBus that events should be passed to.
	 * @param processingMode
	 * 			Defaults to Normal
	 * @param recursion
	 * 			Defaults to None
	 * @param filter
	 * 			Defaults to null
	 * @param manifest
	 * 			Implicit manifest
	 * @return
	 * 		EventHandler generated and added
	 */
	def bus[E <: Event](objectBus: ObjectBus, processingMode: ProcessingMode = ProcessingMode.Normal, recursion: Recursion = Recursion.None, filter: E => Boolean = null)(implicit manifest: Manifest[E]) = {
		val handler = EventHandler(new Function1[E, Unit] {
			def apply(evt: E) = {
				objectBus.process(evt)
			}
		}, processingMode, recursion, filter)
		listeners += handler
		
		handler
	}
}

object Listenable {
	def listenTo[E <: Event](handler: EventHandler, listenables: Listenable*)(implicit manifest: Manifest[E]) = listenables.foreach(l => l.listeners += handler)
}
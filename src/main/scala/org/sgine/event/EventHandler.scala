package org.sgine.event

import org.sgine.core.ProcessingMode

import org.sgine.log._

import org.sgine.work.Worker

import scala.reflect.Manifest

/**
 * EventHandler is a wrapper / supporting class for event listeners.
 * The EventHandler defines information about what and how events will
 * be processed against the encapsulated listener.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EventHandler private (val listener: Event => Unit) {
	/**
	 * ProcessingMode defines the way events will be processed
	 * through this EventHandler. In Normal (default) events
	 * are processed asynchronously but block for the listenable.
	 * This means that multiple events fired for the same object
	 * will stack up and process in the proper order waiting for
	 * each to finish processing normally before moving to the
	 * next. In Blocking mode events are processed in the same
	 * thread that dispatched the event. This is useful for
	 * real-time event handling, but under no circumstances should
	 * a blocking EventHandler do anything that takes much time
	 * to process. Asynchronous mode will process events completely
	 * asynchronously not blocking anything. This is useful when
	 * you need to do something as the result of an event but it
	 * is not dependent on any outside factors.
	 * 
	 * Defaults to Normal
	 */
	var processingMode: ProcessingMode = ProcessingMode.Normal
	/**
	 * Recursion defines the scope of the events being listened
	 * for hierarchically. See the Recursion enum for more information.
	 */
	var recursion: Recursion = Recursion.None
	/**
	 * <code>worker</code>, if set, delegates invocation of the
	 * listener to within that Thread. Defaults to null.
	 */
	var worker: Worker = _
	
	def process(evt: Event) = {
		try {
			if (worker != null) {
				worker.invokeAndWait(() => listener(evt))
			} else {
				listener(evt)
			}
		} catch {
			case exc => trace("Exception thrown in EventHandler", exc)
		}
	}
}

object EventHandler {
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode = ProcessingMode.Normal, recursion: Recursion = Recursion.None, filter: E => Boolean = null, worker: Worker = null)(implicit manifest: Manifest[E]): EventHandler = {
		if ((processingMode == ProcessingMode.Asynchronous) && (recursion != Recursion.None)) {
			throw new RuntimeException("Invalid configuration of EventHandler. In Asynchronous mode recursion is not supported.")
		}
		
		val eh = new EventHandler(EventListener(listener, filter))
		eh.processingMode  = processingMode
		eh.recursion = recursion
		eh.worker = worker
		eh
	}
}
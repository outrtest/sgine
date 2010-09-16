package org.sgine.event

import org.sgine.core.ProcessingMode

import org.sgine.work.Worker

import scala.reflect.Manifest

class EventHandler private (val listener: Event => Unit) {
	var processingMode : ProcessingMode = ProcessingMode.Normal
	var recursion : Recursion = Recursion.None
	/**
	 * <code>worker</code>, if set, delegates invocation of the
	 * listener to within that Thread. Defaults to null.
	 */
	var worker: Worker = _
	
	def process(evt: Event) = {
		if (worker != null) {
			worker.invokeAndWait(() => listener(evt))
		} else {
			listener(evt)
		}
	}
}

object EventHandler {
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode = ProcessingMode.Normal, recursion: Recursion = Recursion.None, filter: E => Boolean = null, worker: Worker = null)(implicit manifest: Manifest[E]): EventHandler = {
		val eh = new EventHandler(EventListener(listener, filter))
		eh.processingMode  = processingMode
		eh.recursion = recursion
		eh.worker = worker
		eh
	}
}
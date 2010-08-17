package org.sgine.event

import scala.reflect.Manifest

class EventHandler private (val listener: Event => Unit) {
	var processingMode : ProcessingMode = ProcessingMode.Normal
	var recursion : Recursion = Recursion.None
	var filter: Event => Boolean = _
	
	def process(evt: Event) = {
		if (isValid(evt)) {
			listener(evt)
		}
	}
	
	def isValid(evt: Event): Boolean = {
		if (filter != null) {
			filter(evt)
		} else {
			true
		}
	}
}

object EventHandler {
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode = ProcessingMode.Normal, recursion: Recursion = Recursion.None, filter: Event => Boolean = null)(implicit manifest: Manifest[E]): EventHandler = {
		val eh = new EventHandler(EventListener(listener))
		eh.processingMode  = processingMode
		eh.recursion = recursion
		eh.filter = filter
		eh
	}
}
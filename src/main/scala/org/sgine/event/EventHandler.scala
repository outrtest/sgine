package org.sgine.event

class EventHandler private (val listener: Event => Unit) {
	var processingMode = ProcessingMode.Normal
	var recursion = Recursion.None
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
	def apply[E <: Event](listener: E => Unit): EventHandler = new EventHandler(EventListener(listener))
	
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode.Value): EventHandler = apply(listener, processingMode, Recursion.None, null)
	
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode.Value, recursion: Recursion.Value): EventHandler = apply(listener, processingMode, recursion, null)
	
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode.Value, recursion: Recursion.Value, filter: Event => Boolean): EventHandler = {
		val eh = new EventHandler(EventListener(listener))
		eh.processingMode  = processingMode
		eh.recursion = recursion
		eh.filter = filter
		eh
	}
}
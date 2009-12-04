package org.sgine.event

class EventHandler private (val listener: Event => Unit) {
	var processingMode = ProcessingMode.Normal
	var recursive = false
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
	
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode.Value): EventHandler = apply(listener, processingMode, false, null)
	
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode.Value, recursive: Boolean): EventHandler = apply(listener, processingMode, recursive, null)
	
	def apply[E <: Event](listener: E => Unit, processingMode: ProcessingMode.Value, recursive: Boolean, filter: Event => Boolean): EventHandler = {
		val eh = new EventHandler(EventListener(listener))
		eh.processingMode  = processingMode
		eh.recursive = recursive
		eh.filter = filter
		eh
	}
}
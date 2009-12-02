package com.sgine.event

class Event (val listenable: Listenable)

object Event {
	def enqueue(evt: Event) = {
		// Pre-Process Event on Listenable
		evt.listenable.preProcessEvent(evt)
		
		// Process Event on blocking handlers
		
	}
}
package com.sgine.event

class EventHandler(val listener: Event => Unit) {
	var processingMode = ProcessingMode.Normal
	
	def process(evt: Event) = {
		
	}
}
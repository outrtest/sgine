package com.sgine.event

class EventHandler(val listener: Event => Unit) {
	var processingMode = ProcessingMode.Normal
	var recursive = false
	
	def process(evt: Event) = {
		
	}
}
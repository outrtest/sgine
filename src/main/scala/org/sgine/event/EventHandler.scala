package org.sgine.event

class EventHandler(val listener: Event => Unit) {
	var processingMode = ProcessingMode.Normal
	var recursive = false
	var filter:(Event) => Boolean = _
	
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
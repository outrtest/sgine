package org.sgine.event

trait Listenable {
	def parent: Listenable
	val listeners: EventProcessor
	
	def processEvent(evt: Event): Unit = {
	}
	
	def processChildEvent(evt: Event): Unit = {
	}
	
	def processParentEvent(evt: Event): Unit = {
	}
}
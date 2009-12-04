package org.sgine.event

trait Listenable {
	def parent: Listenable
	val listeners: EventProcessor
	
	def preProcessEvent(evt:Event):Unit = {
	}
	
	def processEvent(evt:Event):Unit = {
	}
	
	def postProcessEvent(evt:Event):Unit = {
	}
}
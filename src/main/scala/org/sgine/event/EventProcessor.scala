package org.sgine.event

import java.util.concurrent._

import scala.collection.JavaConversions._

final class EventProcessor(listenable: Listenable) extends Iterable[EventHandler] {
	private val handlers = new CopyOnWriteArraySet[EventHandler]
	
	def +=[E <: Event](listener: E => Unit): EventHandler = {
		val h = EventHandler(EventListener(listener))
		this += h
	}
	
	def +=(handler: EventHandler): EventHandler = {
		if (!handlers.contains(handler)) {
			handlers.add(handler)
		}
		handler
	}
	
	def -=(handler: EventHandler): EventHandler = {
		if (handler != null) {
			handlers.remove(handler)
		}
		handler
	}
	
	def iterator: Iterator[EventHandler] = handlers.iterator()
}
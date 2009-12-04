package com.sgine.event

import java.util.concurrent._

import scala.collection.JavaConversions._

final class EventProcessor(listenable:Listenable) extends Iterable[EventHandler] {
	private val handlers = new CopyOnWriteArraySet[EventHandler]
	
	def +=[E <: Event](listener: E => Unit): EventHandler = {
		val h = new EventHandler(EventListener(listener))
		this += h
	}
	
	def +=(handler: EventHandler): EventHandler = {
		handlers.add(handler)
		handler
	}
	
	def -=(listener: Event => Unit): EventHandler = {
		val h = getHandler(listener)
		this -= h
	}
	
	def -=(handler: EventHandler): EventHandler = {
		if (handler != null) {
			handlers.remove(handler)
		}
		handler
	}
	
	def getHandler(listener: Event => Unit): EventHandler = {
		for (h <- handlers) {
			if (h.listener == listener) {
				return h
			}
		}
		null
	}
	
	def iterator: Iterator[EventHandler] = handlers.iterator()
}
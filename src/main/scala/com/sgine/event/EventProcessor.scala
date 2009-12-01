package com.sgine.event

import java.util.concurrent._

import scala.collection.JavaConversions._

final class EventProcessor {
	private val handlers = new CopyOnWriteArraySet[EventHandler]
	
	private[event] def processEvent(evt: Event): Unit = {
		handlers.foreach(_.process(evt))
	}
	
	def +=(listener: Event => Unit): EventHandler = {
		val h = new EventHandler(listener)
		handlers.add(h)
		h
	}
	
	def -=(listener: Event => Unit): EventHandler = {
		for (h <- handlers) {
			if (h.listener == listener) {
				handlers.remove(h)
				return h
			}
		}
		null
	}
	
	def length = handlers.size()
}
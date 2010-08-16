package org.sgine.event

final class EventProcessor(listenable: Listenable) extends Iterable[EventHandler] {
	private var handlers: List[EventHandler] = Nil
	
	def +=[E <: Event](listener: E => Unit): EventHandler = {
		val h = EventHandler(EventListener(listener))
		this += h
	}
	
	def +=(handler: EventHandler): EventHandler = {
		if (handler == null) throw new NullPointerException("EventHandler must not be null")
		synchronized {
			if (!handlers.contains(handler)) {
				handlers = handler :: handlers
			}
		}
		handler
	}
	
	def -=(handler: EventHandler): EventHandler = {
		synchronized {
			if (handler != null) {
				handlers = handlers.filterNot(_ == handler)
			}
		}
		handler
	}
	
	def iterator = handlers.iterator
}
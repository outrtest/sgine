package org.sgine.event

final class EventProcessor(listenable: Listenable) extends Iterable[EventHandler] {
	private var handlers: List[EventHandler] = Nil
	
	def +=[E <: Event](listener: E => Unit): EventHandler = {
		val h = EventHandler(EventListener(listener))
		this += h
	}
	
	def +=(handler: EventHandler): EventHandler = {
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
				handlers -= handler
			}
		}
		handler
	}
	
	def iterator = handlers.iterator
}
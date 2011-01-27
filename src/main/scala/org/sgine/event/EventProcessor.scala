package org.sgine.event

import scala.reflect.Manifest

@serializable
final class EventProcessor(listenable: Listenable) extends Iterable[EventHandler] {
	@transient private var handlers: List[EventHandler] = Nil
	
	def +=[E <: Event](listener: E => Unit)(implicit manifest: Manifest[E]): EventHandler = {
		val h = EventHandler(EventListener(listener))
		this += h
	}
	
	def +=(handler: EventHandler): EventHandler = {
		if (handler == null) throw new NullPointerException("EventHandler must not be null")
		synchronized {
      if (handlers == null) handlers = Nil
			if (!handlers.contains(handler)) {
				handlers = handler :: handlers
			}
		}
		handler
	}
	
	def -=(handler: EventHandler): EventHandler = {
		synchronized {
			if (handler != null) {
        if (handlers == null) handlers = Nil
				handlers = handlers.filterNot(_ == handler)
			}
		}
		handler
	}
	
	def iterator = {
    if (handlers == null) handlers = Nil
    handlers.iterator
  }
}
package org.sgine.event

class EventListener[E <: Event] private (val eventClass:Class[E], val f: Function1[E, Unit]) extends Function1[Event, Unit] {
	def apply(evt: Event) = {
		if (isValidEvent(evt)) {
			f(evt.asInstanceOf[E])
		}
	}
	
	def isValidEvent(evt: Event) = eventClass.isAssignableFrom(evt.getClass)
}

object EventListener {
	def apply[E <: Event](f: E => Unit): Event => Unit = {
		val eventClass:Class[E] = EventListener.determineEventClass(f)
		if (eventClass == classOf[Event]) {
			// Don't wrap in EventListener if Event
			f.asInstanceOf[Function1[Event, Unit]]
		} else {
			// Wrap in EventListener for Event sub-classing
			new EventListener[E](eventClass, f)
		}
	}
	
	private def determineEventClass[E <: Event](f: Function1[E, Unit]):Class[E] = {
		for (m <- f.getClass.getMethods) {
			if (m.getName == "apply") {
				if (m.getParameterTypes.length == 1) {
					if (classOf[Event].isAssignableFrom(m.getParameterTypes()(0))) {
						return m.getParameterTypes()(0).asInstanceOf[Class[E]]
					}
				}
			}
		}
		null
	}
}
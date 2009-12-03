package com.sgine.event

class EventListener[E <: Event] private (f: Function1[E, Unit]) extends Function1[Event, Unit] {
	lazy val eventClass:Class[E] = determineEventClass()
	
	def apply(evt: Event) = {
		if (isValidEvent(evt)) {
			f(evt.asInstanceOf[E])
		}
	}
	
	def isValidEvent(evt: Event) = eventClass == evt.getClass
	
	private def determineEventClass():Class[E] = {
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

object EventListener {
	def apply[E <: Event](f:E => Unit):EventListener[E] = {
		new EventListener[E](f)
	}
}
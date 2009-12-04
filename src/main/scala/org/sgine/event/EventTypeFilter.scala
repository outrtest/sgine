package org.sgine.event

class EventTypeFilter private (eventType: Class[_], inclusive: Boolean) extends Function1[Event, Boolean] {
	def apply(evt: Event): Boolean = {
		if (inclusive) {
			eventType.isAssignableFrom(evt.getClass)
		} else {
			evt.getClass() == eventType
		}
	}
}

object EventTypeFilter {
	def apply(eventType: Class[_], inclusive: Boolean): EventTypeFilter = new EventTypeFilter(eventType, inclusive)
	
	def apply(eventType: Class[_]): EventTypeFilter = apply(eventType, true)
}
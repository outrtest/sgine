package com.sgine.event

case class EventTypeFilter(eventType: Class[_]) extends Function1[Event, Boolean] {
	def apply(evt: Event): Boolean = {
		// TODO: make compliant for extending classes
		evt.getClass() == eventType
	}
}
package org.sgine.event

import scala.reflect.Manifest

class EventListener[E <: Event] private(val f: Function1[E, Unit], val filter: Function1[E, Boolean])(implicit manifest: Manifest[E]) extends Function1[Event, Unit] {
	def apply(evt: Event) = {
		if (isValidEvent(evt)) {
			if ((filter == null) || (filter(evt.asInstanceOf[E]))) {
				f(evt.asInstanceOf[E])
			}
		}
	}
	
	def isValidEvent(evt: Event) = manifest.erasure.isInstance(evt)
}

object EventListener {
	def apply[E <: Event](f: E => Unit, filter: E => Boolean = null)(implicit manifest: Manifest[E]): Event => Unit = {
		val eventClass: Class[E] = manifest.erasure.asInstanceOf[Class[E]]
		if ((eventClass == classOf[Event]) && (filter == null)) {
			// Don't wrap in EventListener if Event
			f.asInstanceOf[Function1[Event, Unit]]
		} else {
			// Wrap in EventListener for Event sub-classing
			new EventListener[E](f, filter)
		}
	}
}
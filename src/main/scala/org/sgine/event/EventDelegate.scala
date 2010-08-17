package org.sgine.event

import scala.reflect.Manifest

/**
 * EventDelegate functions as a listener that delegates events out
 * to another listenable.
 */
class EventDelegate[E <: Event] private (listenable: Listenable)(implicit manifest: Manifest[E]) extends (E => Unit) {
	def apply(evt: E) = {
		Event.enqueue(evt, listenable)
	}
}

object EventDelegate {
	def apply(listenable: Listenable) = new EventDelegate[Event](listenable)
}
package org.sgine.event

/**
 * EventDelegate functions as a listener that delegates events out
 * to another listenable.
 */
class EventDelegate[E <: Event] private (listenable: Listenable) extends (E => Unit) {
	def apply(evt: E) = {
		Event.enqueue(evt, listenable)
	}
}

object EventDelegate {
	def apply(listenable: Listenable) = new EventDelegate(listenable)
}
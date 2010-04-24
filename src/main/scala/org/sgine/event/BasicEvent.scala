package org.sgine.event

class BasicEvent(listenable: Listenable) extends Event(listenable) {
	def retarget(target: Listenable): Event = throw new UnsupportedOperationException("BasicEvent does not support retargetting")
}
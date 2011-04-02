package org.sgine.event

class EventHandler

object EventHandler {
  def apply[E <: Event](f: E => Unit): EventHandler = null
}
package org.sgine.event

import org.sgine.bus.Bus

/**
 * Event is the core object sent to listeners.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/3/11
 */
trait Event {
  private var _target: Listenable = _

  final def target = _target

  val thread = Thread.currentThread()
}

object Event {
  def apply(target: Listenable) = new BasicEvent

  def fire(event: Event, target: Listenable) = {
    if (event._target != null) throw new RuntimeException("Events can only be fired once!")
    event._target = target
    Bus(event)
  }
}

class BasicEvent extends Event
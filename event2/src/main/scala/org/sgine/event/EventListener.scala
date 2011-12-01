package org.sgine.event

import org.sgine.Priority

/**
 * 
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EventListener[E](val listener: (E) => Unit,
                       val priority: Priority = Priority.Normal) {
  def apply(event: E) = listener(event)
}

object EventListener {
  def apply[E](priority: Priority = Priority.Normal)(listener: PartialFunction[E, Unit]) = {
    new EventListener(Listenable.withFallthrough(listener), priority)
  }
}
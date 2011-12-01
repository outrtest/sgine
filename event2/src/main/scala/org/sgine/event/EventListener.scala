package org.sgine.event

import org.sgine.Priority

/**
 * 
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EventListener[E](listener: (E) => Unit, val priority: Priority = Priority.Normal) extends Function1[E, Unit] {
  def apply(event: E) = listener(event)
}

object EventListener {
  def apply[E](priority: Priority = Priority.Normal)(listener: PartialFunction[E, Unit]) = {
    new EventListener(Listenable.withFallthrough(listener), priority)
  }
}
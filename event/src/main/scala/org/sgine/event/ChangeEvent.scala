package org.sgine.event

import org.sgine.Priority

/**
 * ChangeEvent represents a change of value on the target.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class ChangeEvent(target: Listenable, oldValue: Any, newValue: Any) extends Event

object ChangeEvent {
  private val allFilter = (l: Listenable) => true

  // TODO: ignore side-effects of changes? Event.cause stack?
  def record(listenable: Listenable, depth: Int = Int.MaxValue, filter: Listenable => Boolean = allFilter)(action: => Any) = {
    var list = List.empty[Change]
    val listener = listenable.listeners.filter.descendant(depth).priority(Priority.High).synchronous {
      case evt: ChangeEvent => {
        val change = list.find(c => c.listenable == evt.target) match {
          case Some(c) => {
            list = list.filterNot(i => i == c)    // Remove existing entry
            c.copy(newValue = evt.newValue)       // Duplicate with new value
          }
          case None => Change(evt.target, evt.oldValue, evt.newValue)   // Create a new change
        }
        list = change :: list
      }
    }
    try {
      action
    } finally {
      listenable.listeners -= listener
    }
    list.reverse
  }
}

case class Change(listenable: Listenable, oldValue: Any, newValue: Any)
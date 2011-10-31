package org.sgine

/**
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Listenable[T] {
  private var _listeners: List[Listener[T]] = Nil

  object listeners {
    def +=(listener: Listener[T]) = synchronized {
      _listeners = listener :: _listeners
    }

    def -=(listener: Listener[T]) = synchronized {
      _listeners = _listeners.filterNot(l => l eq listener)
    }
  }

  def onChange(f: => Unit) = {
    val listener = (oldValue: T, newValue: T) => f
    listeners += listener
    listener
  }

  def changed(oldValue: T, newValue: T) = {
    _listeners.foreach(listener => listener(oldValue, newValue))
  }
}
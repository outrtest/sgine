package org.sgine

/**
 *  Listenable is a basic non-event listening infrastructure to allow immediate response to changes
 *  on this class.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Listenable[T] {
  private var _listeners: List[Listener[T]] = Nil

  object listeners {
    /**
     * Adds a listener to be invoked on change.
     */
    def +=(listener: Listener[T]) = synchronized {
      _listeners = listener :: _listeners
    }

    /**
     * Removes a listener from this Listenable.
     */
    def -=(listener: Listener[T]) = synchronized {
      _listeners = _listeners.filterNot(l => l eq listener)
    }
  }

  /**
   * Convenience method to be invoke the supplied function upon change.
   */
  def onChange(f: => Unit) = {
    val listener = (oldValue: T, newValue: T) => f
    listeners += listener
    listener
  }

  /**
   * Called when the value changes.
   */
  def changed(oldValue: T, newValue: T) = {
    _listeners.foreach(listener => listener(oldValue, newValue))
  }
}
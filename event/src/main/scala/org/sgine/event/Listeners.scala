package org.sgine.event

import org.sgine.concurrent.Time

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
class Listeners(listenable: Listenable) {
  protected[event] var listeners: List[Listener] = Nil

  def +=(listener: Listener) = synchronized {
    val l = createListener(listener)
    listeners = (l :: listeners.reverse).reverse
    l
  }

  def -=(listener: Listener) = synchronized {
    listeners = listeners.filterNot(l => l == listener)
    listener
  }

  def length = listeners.length

  def isEmpty = listeners.isEmpty

  def nonEmpty = listeners.nonEmpty

  def clear() = synchronized {
    listeners = Nil
  }

  protected def createListener(listener: Listener) = listener

  def apply(f: PartialFunction[Event, Any]) = {
    val function = Listener.withFallthrough(f)
    this += new Listener {
      def process(event: Event) = function(event)
    }
  }

  def filtered[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    this += new FunctionalListener[T](Listener.withFallthrough(f))
  }

  def once[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    var listener: Listener = null
    listener = new FunctionalListener[T](Listener.withFallthrough(f.andThen[Any] {
      case _ => {
        this -= listener
      }
    }))(manifest)
    this += listener
  }

  def waitFor[T <: Event, R](time: Double,
                             precision: Double = 0.01,
                             start: Long = System.currentTimeMillis,
                             errorOnTimeout: Boolean = false)
                            (f: PartialFunction[T, R])(implicit manifest: Manifest[T]): Option[R] = {
    var result: Option[R] = None
    var finished = false
    val function = f.lift
    var listener: Listener = null
    listener = new FunctionalListener[T]((event: T) => {
      if (!finished) {
        function(event) match {
          case None => // Nothing
          case s => {
            this -= listener
            result = s
            finished = true
          }
        }
      }
    })
    this += listener
    Time.waitFor(time, precision, start, errorOnTimeout) {
      result != None
    }
    this -= listener
    result
  }
}
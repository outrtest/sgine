package org.sgine.event

import org.sgine.concurrent.Time
import org.sgine.bus.Bus
import org.sgine.hierarchy.{Parent, Element}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
class Listeners(listenable: Listenable) {
  protected[event] var listeners: List[Listener] = Nil

  def values = listeners

  def +=(listener: Listener) = synchronized {
    val l = createListener(listener)
    listeners = (l :: listeners.reverse).reverse
    Bus.add(l)
    l
  }

  def -=(listener: Listener) = synchronized {
    listeners = listeners.filterNot(l => l == listener)
    Bus.remove(listener)
    listener
  }

  def length = listeners.length

  def isEmpty = listeners.isEmpty

  def nonEmpty = listeners.nonEmpty

  def clear() = synchronized {
    listeners = Nil
  }

  protected def createListener(listener: Listener) = listener

  val acceptFilter = listenable.targetFilter

  def apply(f: PartialFunction[Event, Any])(implicit acceptFilter: Event => Boolean = acceptFilter) = {
    val function = Listener.withFallthrough(f)
    val filter = acceptFilter
    this += new Listener {
      def acceptFilter = filter

      def apply(event: Event) = function(event)
    }
  }

  def descendant(f: PartialFunction[Event, Any])(implicit depth: Int = Int.MaxValue) = {
    val acceptFilter = (event: Event) => event.target match {
      case child: Element if (child.hierarchy.hasAncestor(listenable, depth)) => true
      case _ => false
    }
    apply(f)(acceptFilter)
  }

  def child(f: PartialFunction[Event, Any]) = descendant(f)(1)

  def ancestor(f: PartialFunction[Event, Any])(implicit depth: Int = Int.MaxValue) = {
    val acceptFilter = (event: Event) => event.target match {
      case parent: Parent if (parent.hierarchy.hasDescendant(listenable, depth)) => true
      case _ => false
    }
    apply(f)(acceptFilter)
  }

  def parent(f: PartialFunction[Event, Any]) = ancestor(f)(1)

  def filtered[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    this += new FunctionalListener[T](Listener.withFallthrough(f), listenable.targetFilter)
  }

  def once[T <: Event](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    var listener: Listener = null
    listener = new FunctionalListener[T](Listener.withFallthrough(f.andThen[Any] {
      case _ => {
        this -= listener
      }
    }), listenable.targetFilter)(manifest)
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
    }, listenable.targetFilter)
    this += listener
    Time.waitFor(time, precision, start, errorOnTimeout) {
      result != None
    }
    this -= listener
    result
  }
}
package org.sgine.event

import actors.DaemonActor
import org.sgine.concurrent.{Time, Executor}
import annotation.tailrec
import org.sgine.Priority

/**
 * Listenable can be mixed in to allow events to be fired on it.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Listenable[E] {
  type Listener = (E) => Unit

  protected val associations: List[Listenable[E]] = List(Bus())

  protected lazy val asynchronousActor = new DaemonActor {
    def act() {
      loop {
        react {
          case event: E => fireOn(event, listeners.asynchronous._listeners)
        }
      }
    }
  }.start()

  object listeners {
    object synchronous extends Listeners[E]

    object asynchronous extends Listeners[E]

    object concurrent extends Listeners[E] {
      override def +=(listener: EventListener[E]) = super.+=(new ConcurrentEventListener(listener))

      private class ConcurrentEventListener[E](listener: (E) => Unit, priority: Priority = Priority.Normal) extends EventListener[E](listener, priority) {
        override def apply(event: E) = Executor.invoke {
          listener(event)
        }
      }
    }

    def length = synchronous.length + asynchronous.length + concurrent.length

    def isEmpty = synchronous.isEmpty && asynchronous.isEmpty && concurrent.isEmpty

    def nonEmpty = !isEmpty
  }

  def fire(event: E) = {
    fireOn(event, listeners.synchronous._listeners)
    fireOn(event, listeners.concurrent._listeners)
    if (listeners.asynchronous._listeners.nonEmpty) {
      asynchronousActor ! event
    }
    fireRecursive(event, associations)
  }

  @tailrec
  protected final def fireRecursive(event: E, associations: List[Listenable[E]]): Unit = {
    if (associations.nonEmpty) {
      val l = associations.head
      fireOn(event, l.listeners.synchronous._listeners)
      if (l.listeners.asynchronous._listeners.nonEmpty) {
        l.asynchronousActor ! event
      }
      fireRecursive(event, associations.tail)
    }
  }

  def fireOn(event: E, listeners: List[Listener]): Unit = {
    if (listeners.nonEmpty) {
      listeners.head(event)
      fireOn(event, listeners.tail)
    }
  }
}

object Listenable {
  protected[event] def withFallthrough[T](f: PartialFunction[T, Unit]) = f.orElse[T, Unit] {
    case _ => // Fall through
  }
}

trait Listeners[E] {
  protected[event] var _listeners: List[EventListener[E]] = Nil

  def +=(listener: EventListener[E]) = synchronized {
    _listeners = (listener :: _listeners.reverse).reverse
    listener
  }

  def -=(listener: EventListener[E]) = synchronized {
    _listeners = _listeners.filterNot(l => l == listener)
    listener
  }

  def length = _listeners.length

  def isEmpty = _listeners.isEmpty

  def nonEmpty = !isEmpty

  def apply(f: PartialFunction[E, Unit]) = this += EventListener()(f)

  def filtered[T](f: PartialFunction[T, Unit])(manifest: Manifest[T]) = {
    val function = Listenable.withFallthrough(f)
    val listener = EventListener[E]() {
      case event: AnyRef if (manifest.erasure.isAssignableFrom(event.getClass)) => {
        function(event.asInstanceOf[T])
      }
    }
    this += listener
  }

  def once(f: PartialFunction[E, Unit]) = {
    var listener: EventListener[E] = null
    listener = this += EventListener[E]() {
      case event => f.andThen[Unit] {
        case _ => this -= listener
      }
    }
    listener
  }

  def waitFor[T <: E](time: Double, precision: Double = 0.01, start: Long = System.currentTimeMillis, errorOnTimeout: Boolean = false)(f: PartialFunction[E, T]): Option[T] = {
    var result: Option[T] = None
    val function = Listenable.withFallthrough(f.andThen[Unit] {
      case r => result = Some(r)
    })
    val listener = this += EventListener[E]() {
      case event => function(event)
    }

    Time.waitFor(time, precision, start, errorOnTimeout) {
      result != None
    }
    this -= listener
    result
  }
}
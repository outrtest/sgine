package org.sgine.event

import actors.DaemonActor
import org.sgine.concurrent.{Time, Executor}
import annotation.tailrec

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

    object synchronous extends DefaultListeners[E]

    object asynchronous extends DefaultListeners[E]

    object concurrent extends Listeners[E] {
      def +=(listener: Listener) = synchronous {
        case event => Executor.invoke {
          listener(event)
        }
      }

      def -=(listener: Listener) = listeners.synchronous -= listener // TODO: support referencing inner
    }

  }

  def fire(event: E) = {
    fireOn(event, listeners.synchronous._listeners)
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
  type Listener = (E) => Unit

  def +=(listener: Listener): Listener

  def -=(listener: Listener): Listener

  def apply(f: PartialFunction[E, Unit]) = this += Listenable.withFallthrough(f)

  def filtered[T](f: PartialFunction[T, Unit])(manifest: Manifest[T]) = {
    val listener = Listenable.withFallthrough(f)
    val function: Listener = {
      case event: AnyRef if (manifest.erasure.isAssignableFrom(event.getClass)) => {
        listener(event.asInstanceOf[T])
      }
    }
    this += function
  }

  def once(f: PartialFunction[E, Unit]): Listener = {
    var listener: Listener = null
    listener = this += f.andThen[Unit] {
      case _ => this -= listener
    }.orElse[E, Unit] {
      case _ => // Do nothing
    }
    listener
  }

  def waitFor(time: Double, precision: Double = 0.01, start: Long = System.currentTimeMillis, errorOnTimeout: Boolean = false)(f: PartialFunction[E, Unit]): Boolean = {
    var invoked = false
    var listener: Listener = null
    listener = this += f.andThen[Unit] {
      case _ => {
        this -= listener
        invoked = true
      }
    }.orElse[E, Unit] {
      case _ => // Do nothing
    }
    Time.waitFor(time, precision, start, errorOnTimeout) {
      invoked
    }
  }
}

class DefaultListeners[E] extends Listeners[E] {
  protected[event] var _listeners: List[Listener] = Nil

  def +=(listener: Listener) = synchronized {
    _listeners = (listener :: _listeners.reverse).reverse
    listener
  }

  def -=(listener: Listener) = synchronized {
    _listeners = _listeners.filterNot(l => l == listener)
    listener
  }
}
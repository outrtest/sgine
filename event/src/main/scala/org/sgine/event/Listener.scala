package org.sgine.event

import org.sgine.concurrent.Executor
import org.sgine.Priority
import org.sgine.bus.{Routing, TypedNode}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
trait Listener extends TypedNode[Event] with Function1[Event, Any] {
  val manifest = Listener.EventManifest

  def apply(event: Event): Any

  def priority = Priority.Normal

  def acceptFilter: Event => Boolean

  final def process(event: Event) = if (acceptFilter(event)) {
    apply(event) match {
      case routing: Routing => routing
      case _ => Routing.Continue
    }
  } else {
    Routing.Continue
  }
}

case class TargetFilter(target: Listenable) extends Function1[Event, Boolean] {
  def apply(event: Event) = event.target == target
}

class FunctionalListener[T <: Event](f: (T) => Any, val acceptFilter: Event => Boolean)(implicit manifest2: Manifest[T]) extends Listener {
  def apply(event: Event) = if (manifest2.erasure.isAssignableFrom(event.getClass)) {
    received(event.asInstanceOf[T])
  }

  def received(event: T) = f(event)
}

class ConcurrentListener(listener: Listener, val acceptFilter: Event => Boolean) extends Listener {
  def apply(event: Event) = Executor.invoke {
    listener.process(event)
  }
}

class AsynchronousListener(listener: Listener, val acceptFilter: Event => Boolean, listenable: Listenable) extends Listener {
  def apply(event: Event) = listenable.asynchronousActor ! event -> listener
}

object Listener {
  val EventManifest = Manifest.classType[Event](classOf[Event])

  protected[event] def withFallthrough[T](f: PartialFunction[T, Any]) = f.orElse[T, Any] {
    case _ => // Fall through
  }
}
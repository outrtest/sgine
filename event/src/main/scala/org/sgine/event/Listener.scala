package org.sgine.event

import org.sgine.concurrent.Executor

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 12/2/11
 */
trait Listener {
  def process(event: Event): Any
}

class FunctionalListener[T <: Event](f: (T) => Any)(implicit manifest: Manifest[T]) extends Listener {
  def process(event: Event) = if (manifest.erasure.isAssignableFrom(event.getClass)) {
    received(event.asInstanceOf[T])
  }

  def received(event: T) = f(event)
}

class ConcurrentListener(listener: Listener) extends Listener {
  def process(event: Event) = Executor.invoke {
    listener.process(event)
  }
}

object Listener {
  protected[event] def withFallthrough[T](f: PartialFunction[T, Any]) = f.orElse[T, Any] {
    case _ => // Fall through
  }
}
package org.sgine.event

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class EventFilter[T](listenable: Listenable[_])(implicit manifest: Manifest[T]) {
  type Listener = (T) => Unit

  def synchronous(f: PartialFunction[T, Unit]) = listenable.listeners.synchronous.filtered(f)(manifest)

  def asynchronous(f: PartialFunction[T, Unit]) = listenable.listeners.asynchronous.filtered(f)(manifest)

  def concurrent(f: PartialFunction[T, Unit]) = listenable.listeners.concurrent.filtered(f)(manifest)
}
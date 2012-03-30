package org.sgine.bind

import org.sgine.event.Listenable


//import org.sgine.Listenable

/**
 * Bindable is an inheritable trait on mutable objects that allows binding to Listenable objects to
 * reflect the changes back to the Bindable.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Bindable[T] extends Function1[T, Unit] with Listenable {
  //  private lazy val binding = new Binding[T](this, targetFilter)

  /**
   * Binds this instance to get changes to <code>listenable</code> when they occur.
   *
   * @param listenable what to bind to
   */
  def bind(listenable: Listenable) = {
    val binding = new Binding[T](this, listenable.targetFilter)
    listenable.listeners.synchronous += binding
    binding
  }

  /**
   * Binds this instance to get changes to <code>listenable</code> when they occur and are converted through the
   * <code>conversion</code> implicit function to represent the correct value.
   *
   * @param listenable what to bind to
   */
  def bindTo[S](listenable: Listenable)(implicit conversion: S => T) = {
    val binding = new Binding[S](conversion.andThen(this), listenable.targetFilter)
    listenable.listeners.synchronous += binding
    binding
  }

  /**
   * Unbinds this instance from changes occurring on <code>listenable</code> when they occur.
   *
   * @param listenable to unbind from
   */
  def unbind(listenable: Listenable) = {
    listenable.listeners.synchronous.values.find(l => l match {
      case binding: Binding[_] => binding.acceptFilter == listenable.targetFilter
    }) match {
      case Some(listener) => listenable.listeners.synchronous -= listener
      case None => // Not found
    }
  }
}
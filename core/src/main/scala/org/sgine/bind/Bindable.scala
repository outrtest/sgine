package org.sgine.bind

//import org.sgine.Listenable

/**
 * Bindable is an inheritable trait on mutable objects that allows binding to Listenable objects to
 * reflect the changes back to the Bindable.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
//trait Bindable[T] extends Function1[T, Unit] {
//  private lazy val binding = new Binding[T](this)
//
//  /**
//   * Binds this instance to get changes to <code>listenable</code> when they occur.
//   *
//   * @param listenable what to bind to
//   */
//  def bind(listenable: Listenable[T]) = {
//    listenable.listeners += binding
//  }
//
//  /**
//   * Binds this instance to get changes to <code>listenable</code> when they occur and are converted through the
//   * <code>conversion</code> implicit function to represent the correct value.
//   *
//   * @param listenable what to bind to
//   */
//  def bind[S](listenable: Listenable[S])(implicit conversion: S => T) = {
//    val binding = new Binding[S](conversion.andThen(this))
//    listenable.listeners += binding
//    binding
//  }
//
//  /**
//   * Unbinds this instance from changes occurring on <code>listenable</code> when they occur.
//   *
//   * @param listenable to unbind from
//   */
//  def unbind(listenable: Listenable[T]) = {
//    listenable.listeners -= binding
//  }
//
//  /**
//   * Unbinds the binding from the listenable.
//   *
//   * @param listenable to unbind from
//   */
//  def unbind[S](listenable: Listenable[S])(implicit binding: Binding[S]) = {
//    listenable.listeners -= binding
//  }
//}
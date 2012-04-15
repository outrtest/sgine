package org.sgine.property

import org.sgine.ChangeInterceptor
import org.sgine.bind.Bindable
import org.sgine.event.{ChangeEvent, Listenable}

/**
 * StandardProperty is the default implementation of mutable properties with change listening and
 * interception.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
abstract class StandardProperty[T](implicit override val parent: PropertyParent)
               extends MutableProperty[T] with Listenable with ChangeInterceptor[T] with Bindable[T] {
  protected def getValue: T

  protected def setValue(value: T): Unit

  def apply(v: T) = {
    val oldValue = getValue
    val newValue = change(oldValue, v)
    if (oldValue != newValue) {
      setValue(newValue)
      fire(ChangeEvent(this, oldValue, newValue))
    }
  }

  def apply() = getValue

  def onChange(f: => Unit) = listeners.synchronous {
    case evt: ChangeEvent[_] => f
  }

  def readOnly: Property[T] = this
}












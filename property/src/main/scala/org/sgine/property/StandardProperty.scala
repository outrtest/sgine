package org.sgine.property

import org.sgine.{ChangeInterceptor, Listenable}
import org.sgine.bind.Bindable


/**
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait StandardProperty[T] extends MutableProperty[T] with Listenable[T] with ChangeInterceptor[T] with Bindable[T] {
  protected def getValue: T

  protected def setValue(value: T): Unit

  def apply(v: T) = {
    val oldValue = getValue
    val newValue = change(oldValue, v)
    setValue(newValue)
    changed(oldValue, newValue)
  }

  def apply() = getValue
}












package org.sgine.property

import backing.{VariableBacking, Backing}
import org.sgine.ChangeInterceptor
import org.sgine.bind.Bindable
import org.sgine.event.{ChangeEvent, Listenable}

/**
 * StandardProperty is the default implementation of mutable properties with change listening and
 * interception.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class StandardProperty[T](val name: String, backing: Backing[T] = new VariableBacking[T])
                                  (implicit override val parent: PropertyParent)
                                    extends MutableProperty[T]
                                    with Listenable
                                    with ChangeInterceptor[T]
                                    with Bindable[T] {
  def apply(v: T) = {
    val oldValue = backing.getValue
    val newValue = change(oldValue, v)
    if (oldValue != newValue) {
      backing.setValue(newValue)
      fire(ChangeEvent(this, oldValue, newValue))
    }
  }

  def apply() = backing.getValue

  def onChange(f: => Unit) = listeners.synchronous {
    case evt: ChangeEvent => f
  }

  def readOnly: Property[T] = this

  override def toString() = "Property[%s](%s)".format(name, value)
}












package org.sgine.ui

import event.{FocusGained, FocusLost}
import org.sgine.property.Property
import org.sgine.property.event.PropertyChangeEvent

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait FocusManager extends AbstractContainer {
  val focused = Property[Focusable]("focused", null)

  // Notify Focusables they gained or lost focus
  focused.listeners.synchronous {
    case evt: PropertyChangeEvent => {
      evt.oldValue match {
        case component: Focusable => component.fire(FocusLost(component, this))
        case _ => // Null
      }
      evt.newValue match {
        case component: Focusable => component.fire(FocusGained(component, this))
        case _ => // Null
      }
    }
  }
}

trait Focusable extends Component {
  def focusManager = hierarchy.ancestor((fm: FocusManager) => true).getOrElse(null)

  def isFocused = focusManager match {
    case null => false
    case fm => fm.focused() == this
  }

  def requestFocus() = focusManager match {
    case null => false
    case fm => {
      fm.focused := this
      true
    }
  }
}
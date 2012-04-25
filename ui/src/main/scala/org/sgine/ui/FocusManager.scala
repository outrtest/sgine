package org.sgine.ui

import event.{FocusGained, FocusLost}
import org.sgine.property.Property
import org.sgine.property.event.PropertyChangeEvent
import org.sgine.input.Key
import org.sgine.input.event.{KeyEvent, KeyDownEvent}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait FocusManager extends AbstractContainer {
  val focused = Property[Focusable]("focused", null)

  def focusPrevious(wrap: Boolean = true) = previousFocusable(wrap) match {
    case previous: Focusable => {
      focused := previous
      true
    }
    case null => false
  }

  def focusNext(wrap: Boolean = true) = nextFocusable(wrap) match {
    case next: Focusable => {
      focused := next
      true
    }
    case null => false
  }

  def previousFocusable(wrap: Boolean = true) = focused() match {
    case null => lastFocusable()
    case f => f.hierarchy.backward((f: Focusable) => true) match {
      case null if (wrap) => lastFocusable()
      case previous => previous
    }
  }

  def nextFocusable(wrap: Boolean = true) = focused() match {
    case null => firstFocusable()
    case f => f.hierarchy.forward((f: Focusable) => true) match {
      case null if (wrap) => firstFocusable()
      case next => next
    }
  }

  def firstFocusable() = hierarchy.forward((f: Focusable) => true)

  def lastFocusable() = {
    var last: Focusable = null
    hierarchy.forward((f: Focusable) => {
      last = f
      false
    })
    last
  }

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

      ui match {
        case null => // Cannot activate until attached to UI
        case ui => ui.focusManager := this
      }
    }
  }

  configureFocusKeys()

  protected def configureFocusKeys() = {
    listeners.filter.ancestor().filter.or(filters.target) {
      case evt: KeyEvent => evt.target match {
        case ui: UI if (ui.focusManager() == this) => {
          if (isNextFocus(evt)) {
            focusNext()           // Focus the next component
          } else if (isPreviousFocus(evt)) {
            focusPrevious()       // Focus the previous component
          } else {
            focused() match {
              case null => // Nothing is focused
              case f => f.fire(evt.duplicate())    // Fire the key event on current focus
            }
          }
        }
        case _ => // Ignore key events on other components
      }
      case _ => // Ignore other events
    }
  }

  protected def isNextFocus(event: KeyEvent) = event match {
    case evt: KeyDownEvent => evt.key == Key.Tab && (!Key.ShiftLeft.pressed && !Key.ShiftRight.pressed)
    case _ => false
  }

  protected def isPreviousFocus(event: KeyEvent) = event match {
    case evt: KeyDownEvent => evt.key == Key.Tab && (Key.ShiftLeft.pressed || Key.ShiftRight.pressed)
    case _ => false
  }
}
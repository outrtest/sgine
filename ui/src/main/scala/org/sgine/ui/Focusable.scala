package org.sgine.ui

import org.powerscala.property.Property

trait Focusable extends Component {
  val focusable = Property[Boolean]("focusable", true)

  def focusManager = hierarchy.ancestor((fm: FocusManager) => true).getOrElse(null)

  def isFocused = focusManager match {
    case null => false
    case fm => fm.focused() == this
  }

  def isCurrentFocusManager = focusManager match {
    case null => false
    case fm => ui match {
      case null => false
      case ui => ui.focusManager() == fm
    }
  }

  def requestFocus() = if (focusable()) {
    focusManager match {
      case null => updateAsync.invokeLater(() => {      // Update focus later
        focusManager match {
          case null => // No focus manager!
          case fm => fm.focused := this
        }
      })
      case fm => fm.focused := this
    }
  }
}
package org.sgine.ui

trait Focusable extends Component {
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

  def requestFocus() = focusManager match {
    case null => updateAsync.invokeLater(() => {      // Update focus later
      focusManager match {
        case null => // No focus manager!
        case fm => fm.focused := this
      }
    })
    case fm => fm.focused := this
  }
}
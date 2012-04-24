package org.sgine.ui

import align.{VerticalAlignment, HorizontalAlignment}
import org.sgine.input.event.KeyDownEvent
import org.sgine.input.{Keyboard, Key}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Debug extends UI {
  val fps = new FPSLabel()
  fps.location(-500.0, 380.0)
  contents += fps

  def helpText = """|+/- to zoom in and out.
  |'F' to toggle fullscreen.
  |Left, Right, Up, and Down to rotate.
  |'R' to reset.
  |Escape to quit.""".stripMargin

  val help = new Label(helpText)
  help.includeInLayout := false
  help.alignment.horizontal := HorizontalAlignment.Left
  help.alignment.vertical := VerticalAlignment.Bottom
  help.location(-500.0, -380.0)
  contents += help

  Keyboard.listeners {
    case evt: KeyDownEvent => evt.key match {
      case Key.Plus => scale.set(scale.x() * 1.5)
      case Key.Minus => scale.set(scale.x() * 0.5)
      case Key.F => fullscreen := !fullscreen()
      case Key.Left => rotation.y -= 5.0
      case Key.Right => rotation.y += 5.0
      case Key.Up => rotation.x -= 5.0
      case Key.Down => rotation.x += 5.0
      case Key.R => {   // TODO: determine reset point
        scale.set(1.0)
        rotation.set(1.0)
      }
      case Key.Escape => exit()
      case _ => // Ignore other keys
    }
  }
}

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

  def helpText = "Press +/- to zoom in and out.\nPress 'f' to toggle fullscreen."

  val help = new Label(helpText)
  help.includeInLayout := false
  help.alignment.horizontal := HorizontalAlignment.Left
  help.alignment.vertical := VerticalAlignment.Bottom
  help.location(-500.0, -380.0)
  contents += help

  Keyboard.listeners {
    case evt: KeyDownEvent => evt.key match {
      case Key.Plus => scale.set(scale.x() + 0.1)
      case Key.Minus => scale.set(scale.x() - 0.1)
      case Key.F => fullscreen := !fullscreen()
      case _ => // Ignore other keys
    }
  }
}

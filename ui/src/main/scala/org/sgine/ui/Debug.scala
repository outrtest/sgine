package org.sgine.ui

import align.{VerticalAlignment, HorizontalAlignment}
import org.sgine.input.event.KeyDownEvent
import org.sgine.input.{Keyboard, Key}
import com.badlogic.gdx.graphics.{OrthographicCamera, PerspectiveCamera}
import org.powerscala.property.event.PropertyChangeEvent

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Debug extends UI {
  def helpText = """|+/- to zoom in and out.
    |'F' to toggle fullscreen.
    |Left, Right, Up, and Down to rotate.
    |'R' to reset.
    |Escape to quit.""".stripMargin

  val debug = new Container() {
    localizeMatrix := true
    includeInLayout := false

    val fps = new FPSLabel()
    fps.location(-500.0, 380.0)
    contents += fps

    val help = new Label(helpText)
    help.alignment.horizontal := HorizontalAlignment.Left
    help.alignment.vertical := VerticalAlignment.Bottom
    help.location(-500.0, -380.0)
    contents += help

    Keyboard.listeners {
      case evt: KeyDownEvent => evt.key match {
        case Key.Plus => ui.scale.set(ui.scale.x() * 1.5)
        case Key.Minus => ui.scale.set(ui.scale.x() * 0.5)
        case Key.F => fullscreen := !fullscreen()
        case Key.Left => ui.rotation.y -= 5.0
        case Key.Right => ui.rotation.y += 5.0
        case Key.Up => ui.rotation.x -= 5.0
        case Key.Down => ui.rotation.x += 5.0
        case Key.R => {   // TODO: determine reset point
          ui.scale.set(1.0)
          ui.rotation.set(1.0)
        }
        case Key.Escape => exit()
        case _ => // Ignore other keys
      }
    }
  }
  listeners.synchronous.filter.descendant(1) {
    case evt: PropertyChangeEvent if (evt.property.name == "camera") => evt.newValue match {
      case camera: PerspectiveCamera => debug.resolution(1024.0, 768.0, false)
      case camera: OrthographicCamera => {
        debug.scale.set(1.0)
      }
    }
  }
  contents += debug
}

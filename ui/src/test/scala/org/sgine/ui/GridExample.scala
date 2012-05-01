package org.sgine.ui

import org.sgine.input.{Key, Keyboard}
import org.sgine.input.event.KeyDownEvent


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object GridExample extends UI {
  perspective()

  val grid = new Grid(10, 10, 50.0, 50.0) {
    val b1 = new Button("Button 1")
    contents += b1

    set(b1, 1, 1)

    Keyboard.listeners.synchronous {
      case evt: KeyDownEvent => evt.key match {
        case Key.Left => moveLeft(b1)
        case Key.Right => moveRight(b1)
        case Key.Up => moveUp(b1)
        case Key.Down => moveDown(b1)
        case _ =>
      }
    }
  }
  contents += grid
}

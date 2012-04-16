package org.sgine.ui

import org.sgine.input.event.KeyDownEvent
import org.sgine.input.{Keyboard, Key}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Debug extends UI {
  Keyboard.listeners {
    case evt: KeyDownEvent if (evt.key == Key.Plus) => {
      camera().translate(0.0f, 0.0f, -10.0f)
      camera().update()
    }
    case evt: KeyDownEvent if (evt.key == Key.Minus) => {
      camera().translate(0.0f, 0.0f, 10.0f)
      camera().update()
    }
  }
}

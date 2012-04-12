package org.sgine.ui

import org.sgine.input.event.MouseEvent


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object MousePickingExample extends UI {
  val image = Image("sgine.png")
  image.location.x := 200.0
  image.listeners.synchronous {
    case evt: MouseEvent => println(evt)
  }
  contents += image
}

package org.sgine.ui

/**
 * ImageExample represents an extremely simple example of displaying an image.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ImageExample extends UI {
  val image = Image("sgine.png")
  image.location.x := 250.0
  image.mouseEvent.synchronous {
    case evt => println("MouseEvent: " + evt)
  }
  contents += image
}
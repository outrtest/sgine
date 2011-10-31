package org.sgine.ui

import org.sgine.Resource
import org.sgine.input.Keyboard

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object UITest extends UI {
  val image = new Image()
  image.load(Resource("backdrop_mountains.jpg"))
  contents += image

  Keyboard.keyEvent.synchronous {
    case evt => println("KeyEvent: %s".format(evt))
  }
}
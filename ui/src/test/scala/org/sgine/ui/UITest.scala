package org.sgine.ui

import org.sgine._
import org.sgine.input.Keyboard
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.Texture

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object UITest extends UI {
  Texture.setEnforcePotImages(false)

  val image = new Image()
  image.load(Resource("backdrop_mountains.jpg"))
  contents += image

  val label = new Label()
  label.location.x := 300.0
  label.location.y := 500.0
  label.text := "Hello World!"
  label.font := new BitmapFont(Resource("arial18.fnt"), false)
  contents += label

  Keyboard.keyEvent.synchronous {
    case evt => println("KeyEvent: %s".format(evt))
  }
}
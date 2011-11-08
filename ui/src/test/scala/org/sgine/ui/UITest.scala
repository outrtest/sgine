package org.sgine.ui

import org.sgine._
import com.badlogic.gdx.graphics.g2d.BitmapFont
import input.event.MouseOverEvent

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object UITest extends UI {
  val image = new Image()
  image.load(Resource("backdrop_mountains.jpg"))
  image.mouseEvent.asynchronous {
    case evt: MouseOverEvent => println("Image: " + evt)
    case _ =>
  }
  contents += image

  val label = new Label()
  label.location.x := 300.0
  label.location.y := 500.0
  label.text := "Hello World!"
  label.font := new BitmapFont(Resource("arial18.fnt"), false)
  contents += label

  val text = new Label()
  text.location.x := 600.0
  text.location.y := 300.0
  text.wrapWidth := 150.0
  text.text :=
    "This is lots of text that should wrap to multiple lines properly. Let's see if it does.\n\nThis should have broken to a new line...did it?"
  text.font := label.font()
  text.mouseEvent.synchronous {
    case evt: MouseOverEvent => println("Label: " + evt)
    case _ =>
  }
  contents += text
}
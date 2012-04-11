package org.sgine.ui

import font.BitmapFont
import org.sgine.Resource

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI {
  val button = new Button()
  contents += button
}

class Button extends AbstractContainer {
  val background = new Scale9(Resource("scale9test.png"), 50.0, 50.0, 450.0, 450.0)
  addChild(background)

  implicit val font = BitmapFont(Resource("arial64.fnt"))
  val label = Label("Test Button")
  label.location.z := 0.01
  addChild(label)
}
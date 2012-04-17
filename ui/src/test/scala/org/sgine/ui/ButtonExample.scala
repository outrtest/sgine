package org.sgine.ui

import font.BitmapFont
import org.sgine.{Color, Resource}


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI with Debug {
  val button = new Button()
  button.padding(20.0, 20.0, 20.0, 20.0)
  contents += button

  button.label.text := "Hello World!"
  button.label.color(Color.Black)
}

class Button extends AbstractContainer {
  val background = new Scale9(Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
  background.includeInLayout := false
  addChild(background)

  implicit val font = BitmapFont(Resource("arial64.fnt"))
  val label = Label("")
  label.location.z := 0.01
  addChild(label)

  onChange(size.width, size.height) {
    background.size(size.width(), size.height())
  }
}
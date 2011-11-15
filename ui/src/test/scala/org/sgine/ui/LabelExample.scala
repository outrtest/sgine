package org.sgine.ui

import font.BitmapFont
import org.sgine.Resource

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object LabelExample extends UI {
  contents += Image("sgine.png")

  val font = BitmapFont(Resource("arial64.fnt"))
  val shape = new ShapeComponent()
  font.textWrap("Hello World!", 100.0, shape)
  contents += shape
}
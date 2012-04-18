package org.sgine.ui

import font.BitmapFont
import style.Stylized
import org.sgine.Resource

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Button extends AbstractContainer with Stylized {
  val background = new Scale9(Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
  background.mouseEnabled := false
  background.name = "background"
  background.size.algorithm := null
  background.includeInLayout := false
  addChild(background)

  implicit val font = BitmapFont(Resource("arial64.fnt"))
  val label = Label("")
  label.mouseEnabled := false
  label.name = "label"
  label.location.z := 0.01
  addChild(label)

  onChange(size.width, size.height) {
    background.size(size.width(), size.height())
  }
}
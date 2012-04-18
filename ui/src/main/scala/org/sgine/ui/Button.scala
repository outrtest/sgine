package org.sgine.ui

import style.Stylized

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Button extends AbstractContainer with Stylized {
  val background = new Scale9() {
    override def name = "background"
  }
  background.mouseEnabled := false
  background.size.algorithm := null
  background.includeInLayout := false
  addChild(background)

  val label = Label("")
  label.mouseEnabled := false
  label.name = "label"
  label.location.z := 0.01
  addChild(label)

  onChange(size.width, size.height) {
    background.size(size.width(), size.height())
  }
}
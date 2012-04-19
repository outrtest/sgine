package org.sgine.ui

import style.Stylized

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Button extends AbstractContainer with Stylized {
  protected[ui] val background = new Scale9() {
    override def name = "background"
    mouseEnabled := false
    size.algorithm := null
    includeInLayout := false
  }
  addChild(background)

  protected[ui] val label = new Label() {
    override def name = "label"
    mouseEnabled := false
    location.z := 0.01
  }
  addChild(label)

  val text = label.text

  onChange(size.width, size.height) {
    background.size(size.width(), size.height())
  }
}
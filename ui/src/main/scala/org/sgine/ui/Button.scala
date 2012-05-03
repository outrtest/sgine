package org.sgine.ui

import style.Stylized
import org.sgine.event.ActionEvent
import org.sgine.input.event.{KeyDownEvent, MousePressEvent}
import org.sgine.input.Key

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Button extends AbstractContainer with Stylized with Focusable {
  def this(text: String) = {
    this()
    this.text := text
  }

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

  listeners.synchronous {
    case evt: MousePressEvent => {
      requestFocus()
      fire(ActionEvent("click"))
    }
    case evt: KeyDownEvent if (evt.key == Key.Enter || evt.key == Key.Space) => fire(ActionEvent("click"))
  }
}
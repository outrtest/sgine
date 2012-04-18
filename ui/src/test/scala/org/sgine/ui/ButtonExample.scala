package org.sgine.ui

import org.sgine.{Color, Resource}


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI with Debug {
  val button = new Button()
  button.padding(20.0, 20.0, 20.0, 20.0)
  button.label.text := "Hello World!"
  button.label.color(Color.Black)
  contents += button

  val hoverStyle = () => {
    button.background.resource := Resource("scale9/windows/button/hover.png")
  }
  val pressedStyle = () => {
    button.background.resource := Resource("scale9/windows/button/pressed.png")
  }
  button.style.mouseOver := hoverStyle
  button.style.mousePress := pressedStyle
}
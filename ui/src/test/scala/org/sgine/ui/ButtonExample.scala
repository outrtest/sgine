package org.sgine.ui

import org.sgine.{Color, Resource}


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI with Debug {
  val button = new Button()
  // TODO: Occurs before attached to button...
  button.background.resource := Resource("scale9/windows/button/normal.png")
  button.background.slice.set(3.0, 3.0, 4.0, 5.0)
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
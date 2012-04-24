package org.sgine.ui

import layout.VerticalLayout
import org.sgine.event.ChangeEvent

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object LayoutExample extends UI {
  val b1 = new Button("Button 1") {
    name = "button1"
  }
  val b2 = new Button("Button 2") {
    name = "button2"
  }
  val b3 = new Button("Button 3") {
    name = "button3"
  }

  contents.addAll(b1, b2, b3)

  layout := new VerticalLayout(spacing = 25.0)
}
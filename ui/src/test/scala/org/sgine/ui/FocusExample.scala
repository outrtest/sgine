package org.sgine.ui

import align.HorizontalAlignment
import layout.VerticalLayout

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object FocusExample extends UI with Debug {
  perspective()
  resolution(1024.0, 768.0, false)

  val b1 = new Button("Button 1")
  val b2 = new Button("Button 2")
  val b3 = new Button("Button 3")

  contents.addAll(b1, b2, b3)

  layout := new VerticalLayout(HorizontalAlignment.Center, 25.0)
}

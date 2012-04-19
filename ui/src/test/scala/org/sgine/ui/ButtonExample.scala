package org.sgine.ui


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI with Debug {
  val button = new Button()
  button.text := "Hello World!"
  contents += button
}
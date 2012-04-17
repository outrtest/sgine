package org.sgine.ui

import align.HorizontalAlignment
import font.BitmapFont
import org.sgine.Resource


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI with Debug {
  val button = new Button()
  button.alignment.horizontal := HorizontalAlignment.Left
  contents += button

  button.label.text := "Hello World!"
}

class Button extends AbstractContainer {
  val background = new Scale9(Resource("scale9test.png"), 50.0, 50.0, 450.0, 450.0)
  addChild(background)

  implicit val font = BitmapFont(Resource("arial64.fnt"))
  val label = Label("")
  label.size.width.listeners.synchronous {
    case evt => println("Width changed! " + label.size.width.name)
  }
  label.location.z := 0.01
  addChild(label)

  println(label.size.width.parent)
  println(label.size.width.name)

//  onChange(label.size.width, label.size.height) {
//    println("Label size changed!")
//    background.size.width := label.size.width()
//    background.size.height := label.size.height()
//  }
}
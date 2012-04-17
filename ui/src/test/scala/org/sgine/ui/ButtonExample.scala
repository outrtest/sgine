package org.sgine.ui

import font.BitmapFont
import org.sgine.{Color, Resource}
import org.sgine.event.ChangeEvent
import org.sgine.property.Property


/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ButtonExample extends UI with Debug {
  val button = new Button()
  button.padding(20.0, 20.0, 20.0, 20.0)
  contents += button

  button.label.text := "Hello World!"
  val changed = ChangeEvent.record(button) {
    button.label.color(Color.Gray)
  }
  changed.foreach {
    case change => {
      println(change.listenable.asInstanceOf[Property[_]].hierarchicalName(button) + " - " + change.oldValue + " to " + change.newValue)
    }
  }
}

class Button extends AbstractContainer {
  val background = new Scale9(Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
  background.includeInLayout := false
  addChild(background)

  implicit val font = BitmapFont(Resource("arial64.fnt"))
  val label = Label("")
  label.name = "label"
  label.location.z := 0.01
  addChild(label)

  onChange(size.width, size.height) {
    background.size(size.width(), size.height())
  }
}
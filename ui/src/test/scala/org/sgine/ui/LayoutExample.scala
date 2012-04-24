package org.sgine.ui

import align.{VerticalAlignment, HorizontalAlignment}
import layout.{HorizontalLayout, VerticalLayout}
import org.sgine.input.event.KeyDownEvent
import org.sgine.input.{Key, Keyboard}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object LayoutExample extends UI with Debug {
  val b1 = new Button("Button 1")
  val b2 = new Button("Button Much Longer 2")
  val b3 = new Button("Button 3")

  contents.addAll(b1, b2, b3)

  val verticalLayout = new VerticalLayout(HorizontalAlignment.Center, 25.0)
  val horizontalLayout = new HorizontalLayout(VerticalAlignment.Middle, 25.0)

  layout := horizontalLayout

  Keyboard.listeners.synchronous {
    case evt: KeyDownEvent => evt.key match {
      case Key.H => layout := horizontalLayout
      case Key.V => layout := verticalLayout
      case _ => // Ignore other keys
    }
  }

  override def helpText = "Press 'v' for vertical layout\nPress 'h' for horizontal layout\n" + super.helpText
}
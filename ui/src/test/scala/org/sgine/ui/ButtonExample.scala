package org.sgine.ui

import font.BitmapFont
import org.sgine.{Color, Resource}
import org.sgine.property.Property
import org.sgine.event.{Change, ChangeEvent}
import annotation.tailrec
import org.sgine.concurrent.Executor


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

  Executor.invoke {
    while (true) {
      Thread.sleep(1000)
      button.style.current := hoverStyle
      Thread.sleep(1000)
      button.style.current := null
    }
  }
}

class Button extends AbstractContainer {
  val background = new Scale9(Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
  background.name = "background"
  background.size.algorithm := null
  background.includeInLayout := false
  addChild(background)

  implicit val font = BitmapFont(Resource("arial64.fnt"))
  val label = Label("")
  label.name = "label"
  label.location.z := 0.01
  addChild(label)

  val style = new Style(this)

  onChange(size.width, size.height) {
    background.size(size.width(), size.height())
  }
}

class Style(component: Component) extends ComponentPropertyParent(component) {
  override val name = "style"

  private val changed = Property[List[Change]]("changed")

  val current = Property[Function0[Any]]("current")

  current.listeners.synchronous {
    case evt: ChangeEvent => {
      if (evt.oldValue != null) {   // We need to revert the previous style before applying the new one
        revert(changed())
      }
      generate()                    // Generate the changed list for later reverting
    }
  }

  @tailrec
  private def revert(changes: List[Change]): Unit = {
    if (changes.nonEmpty) {
      val change = changes.head
      change.listenable match {
        case mutable: Function1[_, _] => {
          mutable.asInstanceOf[Function1[Any, Any]](change.oldValue)
        }
        case _ => println("Ignoring: %s".format(change))    // Ignore non-mutable changes
      }
      revert(changes.tail)
    }
  }

  private def generate() = {
    current() match {
      case null => // No new style to apply
      case f => changed := ChangeEvent.record(component) {
        f()
      }
    }
  }
}
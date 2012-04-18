package org.sgine.ui.style

import org.sgine.ui.{ComponentPropertyParent, Component}
import org.sgine.event.{ChangeEvent, Change}
import annotation.tailrec
import org.sgine.property.Property

/**
 * Style is defined in Stylized trait.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Style(val component: Component) extends ComponentPropertyParent(component) {
  override val name = "style"

  private val changed = Property[List[Change]]("changed", Nil)

  private val current = Property[List[StyleProperty]]("current", Nil)

  def set(p: StyleProperty) = synchronized {
    current := List(p)
  }

  def apply(f: => Any) = {
    val p = StyleProperty("temp")
    p := (() => f)
    set(p)
  }

  def add(p: StyleProperty) = synchronized {
    remove(p)
    current := (p :: current().reverse).reverse
  }

  def remove(p: StyleProperty) = synchronized {
    current := current().filterNot(s => s.name == p.name)
  }

  current.listeners.synchronous {
    case evt: ChangeEvent => {
      revert(changed())             // We need to revert the previous style before applying the new one
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
      case styles => changed := ChangeEvent.record(component) {
        applyStyles(styles)
      }
    }
  }

  @tailrec
  private def applyStyles(styles: List[StyleProperty]): Unit = {
    if (styles.nonEmpty) {
      val s = styles.head
      s()()
      applyStyles(styles.tail)
    }
  }
}
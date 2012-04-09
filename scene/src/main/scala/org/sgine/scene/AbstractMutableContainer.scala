package org.sgine.scene

import event.{ChildRemovedEvent, ChildAddedEvent}
import org.sgine.hierarchy.Element
import collection.mutable.ListBuffer

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class AbstractMutableContainer[T <: Element] extends Container[T] {
  protected val buffer = new ListBuffer[T]

  def contents: Seq[T] = buffer

  protected def addChild(child: T) = synchronized {
    buffer += child

    child match {
      case element: Element => Element.assignParent(element, this)
      case _ =>
    }

    fire(new ChildAddedEvent(this, child))
  }

  protected def removeChild(child: T) = synchronized {
    buffer -= child

    child match {
      case element: Element => Element.assignParent(element, null)
      case _ =>
    }

    fire(new ChildRemovedEvent(this, child))
  }
}
package org.sgine.ui

import org.sgine.scene.AbstractMutableContainer
import org.sgine.event.ChangeEvent
import org.sgine.property.Property
import annotation.tailrec

import scala.math._

/**
 * AbstractContainer provides all the functionality for a Component container, but the mutability of its children is
 * protected to the class for better encapsulation when creating complex Components.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class AbstractContainer extends AbstractMutableContainer[Component] with Component {
  private val childSizeChangeRegex = ".*[.]size[.](width|height|depth)"

  this match {
    case ui: UI => // Ignore UI
    case _ => listeners.synchronous.filter.descendant(3) {
      case event: ChangeEvent => event.target match {
        case property: Property[_] if (property.name != null && property.hierarchicalName().matches(childSizeChangeRegex)) => {
          val component = Component.parentComponent(property)
          if (component.parent == this && component.includeInLayout()) {
            updateLayout()
          }
        }
        case _ => // Ignore
      }
    }
  }

  protected def updateLayout() = updateLayoutAbsolute()

  @tailrec
  private def updateLayoutAbsolute(children: Seq[Component] = contents,
                             minX: Double = 0.0,
                             maxX: Double = 0.0,
                             minY: Double = 0.0,
                             maxY: Double = 0.0,
                             minZ: Double = 0.0,
                             maxZ: Double = 0.0): Unit = {
    if (children.isEmpty) {
      measured.width := (maxX - minX) + padding.left() + padding.right()
      measured.height := (maxY - minY) + padding.top() + padding.bottom()
      measured.depth := maxZ - minZ
    } else {
      val current = children.head
      if (current.includeInLayout()) {
        val minx = min(minX, current.location.actual.x() - (current.size.width() / 2.0))
        val maxx = max(maxX, current.location.actual.x() + (current.size.width() / 2.0))
        val miny = min(minY, current.location.actual.y() - (current.size.height() / 2.0))
        val maxy = max(maxY, current.location.actual.y() + (current.size.height() / 2.0))
        val minz = min(minZ, current.location.actual.z() - (current.size.depth() / 2.0))
        val maxz = max(maxZ, current.location.actual.z() + (current.size.depth() / 2.0))
        updateLayoutAbsolute(children.tail, minx, maxx, miny, maxy, minz, maxz)
      } else {
        updateLayoutAbsolute(children.tail, minX, maxX, minY, maxY, minZ, maxZ)
      }
    }
  }

  override protected[ui] def updateMatrix() = {
    super.updateMatrix()

    contents.foreach(AbstractContainer.updateChildMatrix)
  }

  override protected[ui] def updateColor() = {
    super.updateColor()

    contents.foreach(AbstractContainer.updateChildColor)
  }
}

object AbstractContainer {
  private val updateChildMatrix = (child: Component) => child.updateMatrix()
  private val updateChildColor = (child: Component) => child.updateColor()
}
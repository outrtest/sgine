package org.sgine.ui

import org.sgine.scene.AbstractMutableContainer

/**
 * AbstractContainer provides all the functionality for a Component container, but the mutability of its children is
 * protected to the class for better encapsulation when creating complex Components.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class AbstractContainer extends AbstractMutableContainer[Component] with Component {
  override protected[ui] def updateMatrix() = {
    super.updateMatrix()

    contents.foreach(AbstractContainer.updateChildMatrix)
  }

  override protected[ui] def updateAlpha() = {
    super.updateAlpha()

    contents.foreach(AbstractContainer.updateChildAlpha)
  }
}

object AbstractContainer {
  private val updateChildMatrix = (child: Component) => child.updateMatrix()
  private val updateChildAlpha = (child: Component) => child.updateAlpha()
}
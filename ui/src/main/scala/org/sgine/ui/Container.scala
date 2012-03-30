package org.sgine.ui

import org.sgine.scene.MutableContainer

/**
 * Container is specifically targeted to contain Component children.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Container extends MutableContainer[Component] with Component {
  override protected[ui] def updateMatrix() = {
    super.updateMatrix()

    children.foreach(Container.updateChildMatrix)
  }

  override protected[ui] def updateAlpha() = {
    super.updateAlpha()

    children.foreach(Container.updateChildAlpha)
  }
}

object Container {
  private val updateChildMatrix = (child: Component) => child.updateMatrix()
  private val updateChildAlpha = (child: Component) => child.updateAlpha()
}
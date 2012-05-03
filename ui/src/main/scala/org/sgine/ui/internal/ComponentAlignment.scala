package org.sgine.ui.internal

import org.sgine.ui.align.{DepthAlignment, VerticalAlignment, HorizontalAlignment}
import org.sgine.property.{PropertyParent, Property}
import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentAlignment(val parent: Component) extends PropertyParent {
  val name = "alignment"

  val horizontal = Property[HorizontalAlignment]("horizontal", HorizontalAlignment.Center)
  val vertical = Property[VerticalAlignment]("vertical", VerticalAlignment.Middle)
  val depth = Property[DepthAlignment]("depth", DepthAlignment.Middle)
}
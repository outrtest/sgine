package org.sgine.ui.style

import org.sgine.property.{StandardProperty, PropertyParent}
import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class StyleProperty(name: String)(implicit parent: PropertyParent) extends StandardProperty[Function1[Component, Any]](name)(parent) {
  def action(f: => Any) = apply((c: Component) => f)
}

object StyleProperty {
  def apply(name: String)(implicit parent: PropertyParent) = new StyleProperty(name)(parent)
}
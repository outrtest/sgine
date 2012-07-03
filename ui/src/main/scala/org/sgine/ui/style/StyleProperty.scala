package org.sgine.ui.style

import org.powerscala.property.{StandardProperty, PropertyParent}
import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class StyleProperty(name: String, default: Function1[Component, Any])(implicit parent: PropertyParent) extends StandardProperty[Function1[Component, Any]](name, default)(parent) {
  def action(f: => Any) = apply((c: Component) => f)
}

object StyleProperty {
  def apply(name: String, default: Component => Any = null)(implicit parent: PropertyParent) = new StyleProperty(name, default)(parent)
}
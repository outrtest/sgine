package org.sgine.ui.style

import org.sgine.property.{StandardProperty, PropertyParent}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class StyleProperty(name: String)(implicit parent: PropertyParent) extends StandardProperty[Function0[Any]](name)(parent) {
  def action(f: => Any) = apply(() => f)
}

object StyleProperty {
  def apply(name: String)(implicit parent: PropertyParent) = new StyleProperty(name)(parent)
}
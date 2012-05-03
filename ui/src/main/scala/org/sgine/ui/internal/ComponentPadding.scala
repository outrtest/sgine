package org.sgine.ui.internal

import org.sgine.ui.Component
import org.sgine.property.{PropertyParent, NumericProperty}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentPadding(val parent: Component) extends PropertyParent {
  val name = "padding"

  val top = NumericProperty("top", 0.0)
  val bottom = NumericProperty("bottom", 0.0)
  val left = NumericProperty("left", 0.0)
  val right = NumericProperty("right", 0.0)

  def apply(top: Double = this.top(), bottom: Double = this.bottom(), left: Double = this.left(), right: Double = this.right()) = {
    this.top := top
    this.bottom := bottom
    this.left := left
    this.right := right
  }
}
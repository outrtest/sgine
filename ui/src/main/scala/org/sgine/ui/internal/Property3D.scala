package org.sgine.ui.internal

import org.sgine.property.{NumericProperty, PropertyParent}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Property3D(val name: String, val parent: PropertyParent, dx: Double, dy: Double, dz: Double) extends PropertyParent {
  val x = NumericProperty("x", dx)
  val y = NumericProperty("y", dy)
  val z = NumericProperty("z", dz)

  /**
   * Assigns the default value to these properties.
   */
  def default() = {
    apply(dx, dy, dz)
  }

  /**
   * Modifies the encapsulated values. Defaults to the current value if not specified.
   */
  def apply(x: Double = this.x(), y: Double = this.y(), z: Double = this.z()) = {
    this.x := x
    this.y := y
    this.z := z
  }

  /**
   * Sets x, y, and z to the value supplied.
   */
  def set(value: Double) = apply(value, value, value)
}
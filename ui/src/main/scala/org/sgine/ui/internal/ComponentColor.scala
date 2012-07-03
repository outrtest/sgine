package org.sgine.ui.internal

import org.powerscala.{MutableColor, Color}
import org.powerscala.property.{PropertyParent, NumericProperty, ObjectPropertyParent}
import org.sgine.ui.Component

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentColor(val parent: Component) extends PropertyParent {
  val name = "color"

  val red = NumericProperty("red", 1.0)
  val green = NumericProperty("green", 1.0)
  val blue = NumericProperty("blue", 1.0)
  val alpha = NumericProperty("alpha", 1.0)

  def apply(color: Color, updateAlpha: Boolean = true) = {
    red := color.red
    green := color.green
    blue := color.blue
    if (updateAlpha) {
      alpha := color.alpha
    }
  }

  /**
   * Updates the supplied MutableColor to this color value.
   */
  def update(color: MutableColor) = {
    color.red = red()
    color.green = green()
    color.blue = blue()
    color.alpha = alpha()
  }

  /**
   * Creates an immutable Color from this color value.
   */
  def get() = Color.immutable(red(), green(), blue(), alpha())

  object actual extends ObjectPropertyParent(this) {
    val red = NumericProperty("red", 1.0)
    val green = NumericProperty("green", 1.0)
    val blue = NumericProperty("blue", 1.0)
    val alpha = NumericProperty("alpha", 1.0)
  }
}
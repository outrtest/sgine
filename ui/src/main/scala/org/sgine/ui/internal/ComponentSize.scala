package org.sgine.ui.internal

import org.sgine.property.{PropertyParent, Property, NumericProperty}
import org.sgine.ui.{Component, SizeAlgorithm}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentSize(val parent: Component) extends PropertyParent {
  val name = "size"

  val width = NumericProperty("width", 0.0)
  val height = NumericProperty("height", 0.0)
  val depth = NumericProperty("depth", 0.0)
  /**
   * The algorithm used to update the actual size when the measured size changes.
   *
   * Defaults to SizeAlgorithm.measured
   */
  val algorithm = Property[SizeAlgorithm]("algorithm", SizeAlgorithm.measured)

  /**
   * The measured size of the text.
   */
  val measured = new ComponentMeasured(this)

  def apply(width: Double = this.width(), height: Double = this.height(), depth: Double = this.depth()) = {
    this.width := width
    this.height := height
    this.depth := depth
  }
}

class ComponentMeasured(val parent: ComponentSize) extends PropertyParent {
  val name = "measured"

  val width = NumericProperty("width", 0.0)
  val height = NumericProperty("height", 0.0)
  val depth = NumericProperty("depth", 0.0)
}
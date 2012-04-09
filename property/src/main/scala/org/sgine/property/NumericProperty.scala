package org.sgine.property

import backing.{LocalBacking, AtomicBacking, VolatileVariableBacking, VariableBacking}

/**
 * NumericProperty adds additional convenience methods for dealing with properties that are numeric.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait NumericProperty extends StandardProperty[Double] {
  def +=(value: Double) = apply(this.value + value)

  def -=(value: Double) = apply(this.value - value)

  def *=(value: Double) = apply(this.value * value)

  def /=(value: Double) = apply(this.value * value)
}

object NumericProperty {
  /**
   * Creates a new StandardProperty with VariableBacking.
   */
  def apply(): NumericProperty = new StandardProperty[Double] with VariableBacking[Double] with NumericProperty

  /**
   * Creates a new StandardProperty with VariableBacking and the value supplied.
   */
  def apply(value: Double): NumericProperty = {
    val p = apply()
    p.value = value
    p
  }

  /**
   * Creates a new StandardProperty with VolatileVariableBacking.
   */
  def volatile() = new StandardProperty[Double] with VolatileVariableBacking[Double] with NumericProperty

  /**
   * Creates a new StandardProperty with AtomicBacking.
   */
  def atomic() = new StandardProperty[Double] with AtomicBacking[Double] with NumericProperty

  /**
   * Creates a new StandardProperty with LocalBacking.
   */
  def local() = new StandardProperty[Double] with LocalBacking[Double] with NumericProperty
}
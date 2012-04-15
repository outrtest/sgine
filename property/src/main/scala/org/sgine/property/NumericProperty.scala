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
  def apply()(implicit parent: PropertyParent): NumericProperty = new StandardProperty[Double]()(parent) with VariableBacking[Double] with NumericProperty

  /**
   * Creates a new StandardProperty with VariableBacking and the value supplied.
   */
  def apply(value: Double)(implicit parent: PropertyParent): NumericProperty = {
    val p = apply()(parent)
    p.value = value
    p
  }

  /**
   * Creates a new StandardProperty with VolatileVariableBacking.
   */
  def volatile()(implicit parent: PropertyParent) = new StandardProperty[Double]()(parent) with VolatileVariableBacking[Double] with NumericProperty

  /**
   * Creates a new StandardProperty with AtomicBacking.
   */
  def atomic()(implicit parent: PropertyParent) = new StandardProperty[Double]()(parent) with AtomicBacking[Double] with NumericProperty

  /**
   * Creates a new StandardProperty with LocalBacking.
   */
  def local()(implicit parent: PropertyParent) = new StandardProperty[Double]()(parent) with LocalBacking[Double] with NumericProperty
}
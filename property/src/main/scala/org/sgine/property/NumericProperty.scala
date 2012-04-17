package org.sgine.property

import backing._


/**
 * NumericProperty adds additional convenience methods for dealing with properties that are numeric.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class NumericProperty(name: String, backing: Backing[Double])(implicit override val parent: PropertyParent) extends StandardProperty[Double](name, backing) {
  def +=(value: Double) = apply(this.value + value)

  def -=(value: Double) = apply(this.value - value)

  def *=(value: Double) = apply(this.value * value)

  def /=(value: Double) = apply(this.value * value)
}

object NumericProperty {
  /**
   * Creates a new StandardProperty with VariableBacking.
   */
  def apply(name: String, backing: Backing[Double] = new VariableBacking[Double])(implicit parent: PropertyParent) = {
    new NumericProperty(name, backing)(parent)
  }

  /**
   * Creates a new StandardProperty with VariableBacking and the value supplied.
   */
  def apply(name: String, value: Double)(implicit parent: PropertyParent): NumericProperty = {
    val p = apply(name)(parent)
    p.value = value
    p
  }

  /**
   * Creates a new Property with a value tied to the function supplied.
   */
  def apply(_name: String, f: => Double) = new Property[Double] {
    def name = _name

    def apply() = f
  }

  /**
   * Creates a new StandardProperty with VolatileVariableBacking.
   */
  def volatile(name: String)(implicit parent: PropertyParent) = apply(name, new VolatileVariableBacking[Double])(parent)

  /**
   * Creates a new StandardProperty with AtomicBacking.
   */
  def atomic(name: String)(implicit parent: PropertyParent) = apply(name, new AtomicBacking[Double])(parent)

  /**
   * Creates a new StandardProperty with LocalBacking.
   */
  def local(name: String)(implicit parent: PropertyParent) = apply(name, new LocalBacking[Double])(parent)
}
package org.sgine.property

import backing._
import org.sgine.hierarchy.{Named, Child}
import org.sgine.naming.NamingParent

/**
 * Property represents an object containing a value.
 */
trait Property[T] extends Function0[T] with Child with Named {
  def parent: PropertyParent = null

  protected def initialize(): Unit = {
    parent match {
      case p: NamingParent => p.add(this)
      case _ => // Missed
    }
  }

  /**
   * Retrieves the value of the property.
   */
  def value = apply()
}

object Property {
  /**
   * Creates a new StandardProperty with VariableBacking.
   */
  def apply[T](name: String, backing: Backing[T] = new VariableBacking[T])(implicit parent: PropertyParent) = {
    new StandardProperty[T](name, backing)(parent)
  }

  /**
   * Creates a new StandardProperty with VariableBacking and the value supplied.
   */
  def apply[T](name: String, value: T)(implicit parent: PropertyParent) = {
    val p = apply[T](name)(parent)
    p.value = value
    p
  }

  /**
   * Creates a new Property with a value tied to the function supplied.
   */
  def apply[T](_name: String, f: => T) = new Property[T] {
    def name = _name

    def apply() = f
  }

  /**
   * Creates a new StandardProperty with VolatileVariableBacking.
   */
  def volatile[T](name: String)(implicit parent: PropertyParent) = apply(name, new VolatileVariableBacking[T])(parent)

  /**
   * Creates a new StandardProperty with AtomicBacking.
   */
  def atomic[T](name: String)(implicit parent: PropertyParent) = apply(name, new AtomicBacking[T])(parent)

  /**
   * Creates a new StandardProperty with LocalBacking.
   */
  def local[T](name: String)(implicit parent: PropertyParent) = apply(name, new LocalBacking[T])(parent)
}
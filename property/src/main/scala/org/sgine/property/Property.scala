package org.sgine.property

import backing.{LocalBacking, AtomicBacking, VolatileVariableBacking, VariableBacking}

trait Property[T] extends Function0[T] {
  def value = apply()
}

object Property {
  def apply[T]() = new StandardProperty[T] with VariableBacking[T]

  def apply[T](value: T) = {
    val p = apply[T]()
    p.value = value
    p
  }

  def apply[T](f: => T) = new Property[T] {
    def apply() = f
  }

  def volatile[T]() = new StandardProperty[T] with VolatileVariableBacking[T]

  def atomic[T]() = new StandardProperty[T] with AtomicBacking[T]

  def local[T]() = new StandardProperty[T] with LocalBacking[T]
}










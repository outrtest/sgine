package org.sgine.property.backing

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait LocalBacking[T] {
  private val v = new ThreadLocal[T]()

  protected final def getValue = v.get()

  protected final def setValue(value: T) {
    v.set(value)
  }
}
package org.sgine.property.backing

/**
 * LocalBacking utilizes a ThreadLocal for the backing store.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class LocalBacking[T] extends Backing[T] {
  private val v = new ThreadLocal[T]()

  protected[property] final def getValue = v.get()

  protected[property] final def setValue(value: T) {
    v.set(value)
  }
}
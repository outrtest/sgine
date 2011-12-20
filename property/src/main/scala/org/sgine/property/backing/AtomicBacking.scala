package org.sgine.property.backing

import java.util.concurrent.atomic.AtomicReference

/**
 * AtomicBacking utilizes an AtomicReference for the backing store.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait AtomicBacking[T] {
  private val v = new AtomicReference[T]()

  protected final def getValue = v.get()

  protected final def setValue(value: T) {
    v.set(value)
  }
}
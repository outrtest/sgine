package org.sgine.util

import java.util.concurrent.CopyOnWriteArraySet
import scala.collection.JavaConversions._

/**
 * Encapsulates listener concept.
 */
final class Signal[M] {
  private var listeners: CopyOnWriteArraySet[M => Unit] = null

  def addListener(listener: M => Unit) {
    require(listener != null)

    if (listeners == null) {
      listeners = new CopyOnWriteArraySet[M => Unit]()
    }

    if (!listeners.contains(listener)) {
      listeners.add(listener)
    }
  }
  
  def removeListener(listener: M => Unit) {
    if (listeners != null && listeners.contains(listener)) {
      listeners.remove(listener)
    }
  }

  def send(message : M) {
    listeners foreach{ _(message) }
  }

}

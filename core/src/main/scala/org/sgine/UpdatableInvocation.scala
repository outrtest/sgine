package org.sgine

import java.util.concurrent.locks.ReentrantLock
import annotation.tailrec

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait UpdatableInvocation extends Updatable {
  private val mapLock = new ReentrantLock()
  private var updateMap = Map.empty[AnyRef, () => Unit]

  override def update(delta: Double) = {
    super.update(delta)

    if (!updateMap.isEmpty) {
      mapLock.lock()
      try {
        processMap(updateMap)
        updateMap = Map.empty[AnyRef, () => Unit]
      }
      finally {
        mapLock.unlock()
      }
    }
  }

  @tailrec
  private def processMap(map: Map[AnyRef, () => Unit]): Unit = {
    if (!map.isEmpty) {
      val entry = map.head
      entry._2()
      processMap(map.tail)
    }
  }

  protected def delayedHandling(reference: AnyRef, f: () => Unit) = {
    mapLock.lock()
    try {
      updateMap += reference -> f
    }
    finally {
      mapLock.unlock()
    }
  }
}
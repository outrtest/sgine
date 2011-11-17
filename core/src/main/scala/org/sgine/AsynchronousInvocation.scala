package org.sgine

import java.util.concurrent.locks.ReentrantLock
import annotation.tailrec

/**
 * AsynchronousInvocation defines an infrastructure to inject a function to be invoked at a later
 * time by another thread. This works similarly to Actors except these functions are invoked in a
 * specific thread at a specific state.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class AsynchronousInvocation {
  private val lock = new ReentrantLock()
  private var set = Set.empty[() => Unit]

  /**
   * Invokes all waiting invocations within this method.
   */
  def invokeNow() = {
    if (!set.isEmpty) {
      lock.lock()
      try {
        processSet(set)
        set = Set.empty
      } finally {
        lock.unlock()
      }
    }
  }

  /**
   * Invokes the supplied function later when invokeNow() is called.
   */
  def invokeLater(f: () => Unit) = {
    lock.lock()
    try {
      set += f
    } finally {
      lock.unlock()
    }
  }

  @tailrec
  private def processSet(set: Set[() => Unit]): Unit = {
    if (!set.isEmpty) {
      val entry = set.head
      entry()
      processSet(set.tail)
    }
  }
}
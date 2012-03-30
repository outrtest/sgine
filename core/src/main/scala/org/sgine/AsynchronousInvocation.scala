package org.sgine

import annotation.tailrec
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * AsynchronousInvocation defines an infrastructure to inject a function to be invoked at a later
 * time by another thread. This works similarly to Actors except these functions are invoked in a
 * specific thread at a specific state.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class AsynchronousInvocation {
  private val set = new ConcurrentLinkedQueue[() => Unit]()

  /**
   * Invokes all waiting invocations within this method.
   */
  def invokeNow() = processSet()

  /**
   * Invokes the supplied function later when invokeNow() is called.
   */
  def invokeLater(f: () => Unit) = set.add(f)

  @tailrec
  private def processSet(): Unit = {
    if (!set.isEmpty) {
      set.poll() match {
        case null => // Concurrency
        case entry => entry()
      }
      processSet()
    }
  }
}
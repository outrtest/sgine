/*
 * Copyright (c) 2011 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.event

import org.sgine.{Priority, ProcessingMode}
import java.lang.ref.SoftReference
import java.lang.ThreadLocal
import org.sgine.concurrent.WorkQueue

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
class EventHandler[T](listener: T => Any,
                      val processingMode: ProcessingMode,
                      val priority: Double = Priority.Normal,
                      val recursion: Recursion = Recursion.Current,
                      val workQueue: WorkQueue = null)
                     (implicit val manifest: Manifest[T]) extends Ordered[EventHandler[T]] {
  def invoke(event: T, currentTarget: Listenable) = {
    if (workQueue != null) {
      WorkQueue.enqueue(workQueue, () => invokeFunction(event, currentTarget))
    } else {
      invokeFunction(event, currentTarget)
    }
  }

  private val invokeFunction = (event: T, currentTarget: Listenable) => {
    EventHandler.currentTarget.set(currentTarget)
    EventHandler.currentHandler.set(this)
    listener(event)
    EventHandler.currentTarget.set(null)
    EventHandler.currentHandler.set(null)
  }

  def compare(that: EventHandler[T]) = that.priority.compare(priority)
}

object EventHandler {
  private val currentTarget = new ThreadLocal[Listenable]
  private val currentHandler = new ThreadLocal[EventHandler[_]]

  def apply[T](processingMode: ProcessingMode = ProcessingMode.Synchronous,
               priority: Double = Priority.Normal,
               recursion: Recursion = Recursion.Current)
              (listener: T => Any)
              (implicit manifest: Manifest[T]) = {
    new EventHandler(listener, processingMode, priority, recursion)
  }

  /**
   * Creates a wrapped listener using a SoftReference in order to allow underlying listener
   * to be garbage collected if out of scope and space is needed.
   */
  def soft[T](processingMode: ProcessingMode = ProcessingMode.Synchronous,
              priority: Double = Priority.Normal,
              recursion: Recursion = Recursion.Current)
             (listener: T => Any)
             (implicit manifest: Manifest[T]) = {
    val ref = new SoftReference(listener)
    val softListener = (event: T) => {
      val listener = ref.get
      if (listener == null) {
        currentTarget.get.removeHandler(currentHandler.get)
      }
    }
    new EventHandler(softListener, processingMode, priority, recursion)
  }
}
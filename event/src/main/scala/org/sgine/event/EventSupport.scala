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
import org.sgine.concurrent.{WorkQueue, Time}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
class EventSupport[T](_listenable: Listenable = null) {
  /**
   * If alwaysFire is enabled events will always fire even if there are no listeners attached.
   * This can be useful for hierarchical listening as "shouldFire" does not account for parents
   * listening for children events.
   *
   * Defaults to false
   */
  var alwaysFire = false

  def +=(listener: T => Any)(implicit manifest: Manifest[T]): EventHandler[T] = {
    this += new EventHandler(listener, ProcessingMode.Synchronous)(manifest)
  }

  def +=(handler: EventHandler[T]): EventHandler[T] = {
    listenable.addHandler(handler)
    handler
  }

  def -=(handler: EventHandler[T]): EventHandler[T] = {
    listenable.removeHandler(handler)
    handler
  }

  def size(implicit manifest: Manifest[T]) = listenable.size(manifest.erasure)

  def clear()(implicit manifest: Manifest[T]) = listenable.clear(manifest.erasure)

  def synchronous(f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f.orElse {
      case _ => // Fall-through
    }, ProcessingMode.Synchronous)(manifest)
    this += handler
  }

  def asynchronous(f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f.orElse {
      case _ => // Fall-through
    }, ProcessingMode.Asynchronous)(manifest)
    this += handler
  }

  def concurrent(f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f.orElse {
      case _ => // Fall-through
    }, ProcessingMode.Concurrent)(manifest)
    this += handler
  }

  def listen(processingMode: ProcessingMode = ProcessingMode.Synchronous,
             priority: Priority = Priority.Normal,
             recursion: Recursion = Recursion.Current,
             workQueue: WorkQueue = null)
            (f: PartialFunction[T, Any])
            (implicit manifest: Manifest[T]): EventHandler[T] = {
    val handler = new EventHandler[T](f.orElse {
      case _ => // Fall-through
    }, processingMode, priority, recursion, workQueue)(manifest)
    this += handler
  }

  def hasListeners(implicit manifest: Manifest[T]) = listenable.hasListeners(manifest.erasure)

  /**
   * Will return true if there is a listener attached to this Listenable or the Bus for this type. Additionally,
   * if "alwaysFire" is set to true this will always return true.
   *
   * Note: this does not account for hierarchical listening (such as Recursion.Children on a parent)
   */
  def shouldFire(implicit manifest: Manifest[T]) = {
    listenable.shouldFire(manifest.erasure) || Bus.shouldFire(manifest.erasure) || alwaysFire
  }

  def fire(event: T)(implicit manifest: Manifest[T]) = {
    listenable.fire(manifest.erasure.asInstanceOf[Class[T]], event)
  }

  def waitFor(time: Double)(implicit manifest: Manifest[T]) = {
    var response: Option[T] = None
    val handler = new EventHandler((t: T) => response = Some(t), ProcessingMode.Synchronous)(manifest)
    this += handler
    Time.waitFor(time) {
      response != None
    }
    this -= handler
    response
  }

  def apply(listenable: Listenable) = {
    EventSupport._listenable.set(listenable)
    this
  }

  private def listenable = _listenable match {
    case null => EventSupport._listenable.get()
    case listenable => listenable
  }
}

object EventSupport {
  protected[event] val _listenable = new ThreadLocal[Listenable]
}
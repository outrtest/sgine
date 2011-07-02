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

import org.sgine.ProcessingMode
import org.sgine.concurrent.Time

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 6/21/11
 */
trait EventSupport[T] {
  def +=(listener: T => Any)(implicit manifest: Manifest[T]): EventHandler[T] = {
    this += new EventHandler(listener, ProcessingMode.Synchronous)(manifest)
  }

  def +=(handler: EventHandler[T]): EventHandler[T] = {
    EventSupport.listenable.get().addHandler(handler)
    handler
  }

  def -=(handler: EventHandler[T]): EventHandler[T] = {
    EventSupport.listenable.get().removeHandler(handler)
    handler
  }

  def size(implicit manifest: Manifest[T]) = EventSupport.listenable.get().size(manifest.erasure)

  def clear()(implicit manifest: Manifest[T]) = EventSupport.listenable.get().clear(manifest.erasure)

  def synchronous(f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Synchronous)(manifest)
    this += handler
  }

  def asynchronous(f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Asynchronous)(manifest)
    this += handler
  }

  def concurrent(f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Concurrent)(manifest)
    this += handler
  }

  def hasListeners(implicit manifest: Manifest[T]) = EventSupport.listenable.get().hasListeners(manifest.erasure)

  def shouldFire(implicit manifest: Manifest[T]) = EventSupport.listenable.get().shouldFire(manifest.erasure)

  def fire(event: T)(implicit manifest: Manifest[T]) = {
    EventSupport.listenable.get().fire(manifest.erasure.asInstanceOf[Class[T]], event)
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
    EventSupport.listenable.set(listenable)
    this
  }
}

object EventSupport {
  protected[event] val listenable = new ThreadLocal[Listenable]
}
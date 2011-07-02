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
 * Bus provides a single Listenable to listen for any kind of event.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Bus extends Listenable {
    def +=[T](listener: T => Any)(implicit manifest: Manifest[T]): EventHandler[T] = {
    this += new EventHandler(listener, ProcessingMode.Synchronous)(manifest)
  }

  def +=[T](handler: EventHandler[T]): EventHandler[T] = {
    addHandler(handler)
    handler
  }

  def -=[T](handler: EventHandler[T]): EventHandler[T] = {
    removeHandler(handler)
    handler
  }

  def size[T](implicit manifest: Manifest[T]): Int = size(manifest.erasure)

  def clear[T]()(implicit manifest: Manifest[T]): Unit = clear(manifest.erasure)

  def synchronous[T](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Synchronous)(manifest)
    this += handler
  }

  def asynchronous[T](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Asynchronous)(manifest)
    this += handler
  }

  def concurrent[T](f: PartialFunction[T, Any])(implicit manifest: Manifest[T]) = {
    val handler = new EventHandler[T](f, ProcessingMode.Concurrent)(manifest)
    this += handler
  }

  def hasListeners[T](implicit manifest: Manifest[T]): Boolean = hasListeners(manifest.erasure)

  def waitFor[T](time: Double)(implicit manifest: Manifest[T]) = {
    var response: Option[T] = None
    val handler = new EventHandler((t: T) => response = Some(t), ProcessingMode.Synchronous)(manifest)
    this += handler
    Time.waitFor(time) {
      response != None
    }
    this -= handler
    response
  }
}
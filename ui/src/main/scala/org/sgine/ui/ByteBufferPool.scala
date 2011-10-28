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

package org.sgine.ui

import annotation.tailrec
import java.nio.{ByteOrder, ByteBuffer}
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ConcurrentLinkedQueue
import org.sgine.concurrent.AtomicInt

/**
 * Pool of ByteBuffers
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 7/12/11
 */
object ByteBufferPool {
  private val _created = new AtomicInt(0)
  def created = _created.get
  var maximum = 10
  private val queue = new ConcurrentLinkedQueue[ByteBuffer]()

  def request(size: Int): ByteBuffer = {
    queue.poll() match {
      case null => {
        if (_created.incrementIfLessThan(maximum)) {
          create(size)
        } else {
          Thread.sleep(1)
          request(size)
        }
      }
      case bb if (bb.capacity >= size) => bb
      case bb => {
        try { // Attempt to release the direct ByteBuffer
          bb.getClass.getMethod("cleaner").invoke(bb).asInstanceOf[sun.misc.Cleaner].clean()
        } catch {
          case throwable => // Ignore
        }
        create(size)
      }
    }
  }

  def release(bb: ByteBuffer) = {
    bb.clear()
    queue.add(bb)
  }

  private def create(size: Int) = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())
}
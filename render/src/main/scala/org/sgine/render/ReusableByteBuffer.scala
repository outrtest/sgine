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

package org.sgine.render

import java.nio.{ByteOrder, ByteBuffer}
import java.lang.ThreadLocal

/**
 * Provides a thread-local instance of a direct ByteBuffer to be re-used without creating excess garbage or memory
 * consumption.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
private class ReusableByteBuffer {
  private var byteBuffer: ByteBuffer = _

  def allocate(size: Int) = {
    if (byteBuffer == null || byteBuffer.capacity < size) {
      byteBuffer = ReusableByteBuffer.createBuffer(size)
    }
    byteBuffer.clear()

    byteBuffer
  }
}

object ReusableByteBuffer {
  private val instance = new ThreadLocal[ReusableByteBuffer]

  def apply(size: Int)(f: ByteBuffer => Unit) = {
    val rbb = get
    val bb = rbb.allocate(size)
    f(bb)
  }

  def apply(width: Int, height: Int)(f: ByteBuffer => Unit) = {
    val rbb = get
    val bb = rbb.allocate(width * height * 4)
    f(bb)
  }

  private def get = {
    instance.get() match {
      case null => {
        val rbb = new ReusableByteBuffer
        instance.set(rbb)
        rbb
      }
      case rbb => rbb
    }
  }

  private def createBuffer(size: Int) = {
    ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder)
  }
}
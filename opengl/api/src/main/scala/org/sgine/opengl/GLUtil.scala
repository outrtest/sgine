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

package org.sgine.opengl

import annotation.tailrec
import java.nio._

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 3/26/11
 */
object GLUtil {
  def toFloatBuffer(buffer: Buffer): FloatBuffer = {
    throw new RuntimeException("Not implemented!")
  }

  def toDoubleBuffer(buffer: Buffer): DoubleBuffer = {
    throw new RuntimeException("Not implemented!")
  }

  @tailrec
  def toBuffer(values: List[Float], dimensions: Int = 1, vertices: Int = 1, buffer: FloatBuffer = null): FloatBuffer = {
    if (buffer == null) {
      val b = ByteBuffer.allocateDirect((values.length * vertices) * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
      toBuffer(values, dimensions, vertices, b)
    } else if (!values.isEmpty) {
      var updated = values
      for (v <- 0 until vertices) {
        updated = values
        for (i <- 0 until dimensions) {
          buffer.put(updated.head)
          updated = updated.tail
        }
      }
      toBuffer(updated, dimensions, vertices, buffer)
    } else {
      buffer
    }
  }
}
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

import java.nio.ByteBuffer
import scala.math._
import org.sgine.resource.Resource

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object TextureUtils {
  def apply(resource: Resource, mipmap: Boolean = true) = {
    var texture: Texture = null
    val f = (width: Int, height: Int, buffer: ByteBuffer) => texture = Texture(width, height, mipmap, buffer)
    ImageUtils(resource.url)(f)
    texture
  }

  def create(width: Int, height: Int, mipmap: Boolean, red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 1.0) = {
    var texture: Texture = null
    val size = width * height
    val r = round(red * 255.0).toByte
    val g = round(green * 255.0).toByte
    val b = round(blue * 255.0).toByte
    val a = round(alpha * 255.0).toByte
    val f = (buffer: ByteBuffer) => {
      for (i <- 0 until size) {
        buffer.put(r)
        buffer.put(g)
        buffer.put(b)
        buffer.put(a)
      }
      buffer.flip()
      texture = Texture(width, height, mipmap, buffer)
    }
    ReusableByteBuffer(width, height)(f)
    texture
  }
}
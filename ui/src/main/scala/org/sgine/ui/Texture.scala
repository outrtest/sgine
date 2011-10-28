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

import org.sgine.render.{Texture => RenderTexture}
import java.nio.ByteBuffer
import de.matthiasmann.twl.utils.PNGDecoder
import org.sgine.resource.Resource

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Texture private(val resource: Resource, val width: Int, val height: Int, val mipmap: Boolean) {
  private var buffer: ByteBuffer = _
  lazy val renderTexture = {
    val rt = RenderTexture(width, height, mipmap, buffer)
    ByteBufferPool.release(buffer)
    buffer = null
    rt
  }
}

object Texture {
  /**
   * Loads the image from the URL. Currently only supports PNG files.
   */
  def apply(resource: Resource, mipmap: Boolean = true) = {
    val input = resource.url.openStream()
    try {
      val decoder = new PNGDecoder(input)
      val buffer = ByteBufferPool.request(decoder.getWidth * decoder.getHeight * 4)
			decoder.decode(buffer, decoder.getWidth * 4, PNGDecoder.Format.RGBA)
			buffer.flip()
      val texture = new Texture(resource, decoder.getWidth, decoder.getHeight, mipmap)
      texture.buffer = buffer
      texture
    } finally {
      input.close()
    }
  }
}
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

package org.sgine.render.implementation.opengl

import org.sgine.render.Texture
import java.nio.ByteBuffer

import org.sgine.opengl.GL._

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class OpenGLTexture(val width: Int, val height: Int, mipmap: Boolean) extends Texture {
  var id = -1

  def updateTexture(x1: Int, y1: Int, x2: Int, y2: Int, buffer: ByteBuffer) = {
    val created = id == -1

    id = glGenTexture()
    glBindTexture(GL_TEXTURE_2D, id)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if (mipmap) GL_LINEAR_MIPMAP_LINEAR else GL_LINEAR)
    // TODO: detect if the following two are supported
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

    if (mipmap) {   // TODO: detect if it's supported
      glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE)
    }

    // TODO: add handling for non power of two functionality

    if (created) {
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
    }
    val w = x2 - x1
    val h = y2 - y1
    glTexSubImage2D(GL_TEXTURE_2D, 0, x1, y1, w, h, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
  }

  def bind() = {
    if (id != -1) {
      glBindTexture(GL_TEXTURE_2D, id)    // TODO: support dynamic determination of already bound
      glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
    }
  }

  def unbind() = glBindTexture(GL_TEXTURE_2D, 0)

  def dispose() = {
    if (id != -1) {
      glDeleteTexture(id)
      id = -1
    }
  }
}
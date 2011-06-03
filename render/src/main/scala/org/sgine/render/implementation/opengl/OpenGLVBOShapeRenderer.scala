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

import org.sgine.opengl.GL._
import java.nio.{ByteOrder, ByteBuffer}
import org.sgine.render.Shape

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 * Date: 5/20/11
 */
class OpenGLVBOShapeRenderer extends Shape {
  private var id: Int = -1
  private var length: Int = _

  def updateVertices(vertices: Seq[Float], dynamic: Boolean) = {
    if (id == -1) {
      id = glGenBuffer()
    }
    glBindBuffer(GL_ARRAY_BUFFER, id)

    val size = vertices.length * 4
    val bb = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder)
    val data = bb.asFloatBuffer()
    data.clear()
    vertices.foreach(f => data.put(f))
    data.flip()
    val usage = if (dynamic) GL_STREAM_DRAW else GL_STATIC_DRAW
    glBufferData(GL_ARRAY_BUFFER, size, data, usage)
    length = vertices.length
  }

  def render() = {
    if (id != -1) {
      glEnableClientState(GL_VERTEX_ARRAY)
      glBindBuffer(GL_ARRAY_BUFFER, id)

      glVertexPointer(3, GL_FLOAT, 0, 0)
      glDrawArrays(GL_TRIANGLES, 0, length)
    }
  }

  def dispose() = {
    if (id != -1) {   // Delete existing VBO
      glDeleteBuffer(id)
      id = -1
    }
  }
}
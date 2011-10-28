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

package org.sgine.opengl.nehe

import org.sgine.opengl.GL._
import java.nio.{ByteOrder, ByteBuffer, FloatBuffer}
import org.sgine.opengl.lwjgl.LWJGLController
import org.sgine.opengl.{GLUtil, GLDisplay}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Lesson05 extends GLDisplay {
  private val triangleVertices = Array(0.0f, 1.0f, 0.0f,
                                       -1.0f, -1.0f, 1.0f,
                                       1.0f, -1.0f, 1.0f,
                                       0.0f, 1.0f, 0.0f,
                                       1.0f, -1.0f, 1.0f,
                                       1.0f, -1.0f, -1.0f,
                                       0.0f, 1.0f, 0.0f,
                                       1.0f, -1.0f, -1.0f,
                                       -1.0f, -1.0f, -1.0f,
                                       0.0f, 1.0f, 0.0f,
                                       -1.0f, -1.0f, -1.0f,
                                       -1.0f, -1.0f, 1.0f)
  private val triangleColors = Array(1.0f, 0.0f, 0.0f, 1.0f,
                                     0.0f, 1.0f, 0.0f, 1.0f,
                                     0.0f, 0.0f, 1.0f, 1.0f,
                                     1.0f, 0.0f, 0.0f, 1.0f,
                                     0.0f, 0.0f, 1.0f, 1.0f,
                                     0.0f, 1.0f, 0.0f, 1.0f,
                                     1.0f, 0.0f, 0.0f, 1.0f,
                                     0.0f, 1.0f, 0.0f, 1.0f,
                                     0.0f, 0.0f, 1.0f, 1.0f,
                                     1.0f, 0.0f, 0.0f, 1.0f,
                                     0.0f, 0.0f, 1.0f, 1.0f,
                                     0.0f, 1.0f, 0.0f, 1.0f)
  private var triangleVertexBuffer: FloatBuffer = _
  private var triangleColorBuffer: FloatBuffer = _
  private var triangleRotation = 0.0f

  private val quadVertices = Array(-1.0f, 1.0f, 1.0f,     // Top
                                   -1.0f, 1.0f, -1.0f,
                                   1.0f, 1.0f, 1.0f,
                                   1.0f, 1.0f, -1.0f,
                                   -1.0f, 1.0f, -1.0f,
                                   1.0f, 1.0f, 1.0f,
                                   -1.0f, -1.0f, 1.0f,     // Bottom
                                   -1.0f, -1.0f, -1.0f,
                                   1.0f, -1.0f, 1.0f,
                                   1.0f, -1.0f, -1.0f,
                                   -1.0f, -1.0f, -1.0f,
                                   1.0f, -1.0f, 1.0f,
                                   -1.0f, -1.0f, 1.0f,    // Front
                                   1.0f, -1.0f, 1.0f,
                                   -1.0f, 1.0f, 1.0f,
                                   1.0f, 1.0f, 1.0f,
                                   -1.0f, 1.0f, 1.0f,
                                   1.0f, -1.0f, 1.0f,
                                   -1.0f, -1.0f, -1.0f,   // Back
                                   1.0f, -1.0f, -1.0f,
                                   -1.0f, 1.0f, -1.0f,
                                   1.0f, 1.0f, -1.0f,
                                   -1.0f, 1.0f, -1.0f,
                                   1.0f, -1.0f, -1.0f,
                                   -1.0f, 1.0f, 1.0f,     // Left
                                   -1.0f, 1.0f, -1.0f,
                                   -1.0f, -1.0f, 1.0f,
                                   -1.0f, -1.0f, -1.0f,
                                   -1.0f, -1.0f, 1.0f,
                                   -1.0f, 1.0f, -1.0f,
                                   1.0f, 1.0f, 1.0f,     // Right
                                   1.0f, 1.0f, -1.0f,
                                   1.0f, -1.0f, 1.0f,
                                   1.0f, -1.0f, -1.0f,
                                   1.0f, -1.0f, 1.0f,
                                   1.0f, 1.0f, -1.0f)
  private val quadColors = List(0.0f, 1.0f, 0.0f, 1.0f,
                                 1.0f, 0.5f, 0.0f, 1.0f,
                                 1.0f, 0.0f, 0.0f, 1.0f,
                                 1.0f, 1.0f, 0.0f, 1.0f,
                                 0.0f, 0.0f, 1.0f, 1.0f,
                                 1.0f, 0.0f, 1.0f, 1.0f)
  private var quadVertexBuffer: FloatBuffer = _
  private var quadColorBuffer: FloatBuffer = _
  private var quadRotation = 0.0f

  def create() = {
    glShadeModel(GL_SMOOTH)
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClearDepth(1.0f)
    glEnable(GL_DEPTH_TEST)
    glDepthFunc(GL_LEQUAL)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()

    // Create Triangle
    var bb = ByteBuffer.allocateDirect(triangleVertices.length * 4)
    bb.order(ByteOrder.nativeOrder())
    triangleVertexBuffer = bb.asFloatBuffer()
    triangleVertexBuffer.put(triangleVertices)
    triangleVertexBuffer.position(0)
    bb = ByteBuffer.allocateDirect(triangleColors.length * 4)
    bb.order(ByteOrder.nativeOrder())
    triangleColorBuffer = bb.asFloatBuffer()
    triangleColorBuffer.put(triangleColors)
    triangleColorBuffer.position(0)

    // Create Quad
    bb = ByteBuffer.allocateDirect(quadVertices.length * 4)
    bb.order(ByteOrder.nativeOrder())
    quadVertexBuffer = bb.asFloatBuffer()
    quadVertexBuffer.put(quadVertices)
    quadVertexBuffer.position(0)
    quadColorBuffer = GLUtil.toBuffer(quadColors, 4, 6)
    quadColorBuffer.position(0)
  }

  def resize(width: Int, height: Int) = {
    glViewport(0, 0, width, height)

    gluPerspective(45.0f, width.toFloat / height.toFloat, 0.1f, 100.0f)

    glMatrixMode(GL_MODELVIEW)
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)
  }

  def render() = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glLoadIdentity()

    glTranslatef(-1.5f, 0.0f, -6.0f)
    glRotatef(triangleRotation, 0.0f, 1.0f, 0.0f)

    glVertexPointer(3, GL_FLOAT, 0, triangleVertexBuffer)
    glColorPointer(4, GL_FLOAT, 0, triangleColorBuffer)
    glEnableClientState(GL_VERTEX_ARRAY)
    glEnableClientState(GL_COLOR_ARRAY)
    glDrawArrays(GL_TRIANGLES, 0, triangleVertices.length / 3)

    glLoadIdentity()
    glTranslatef(1.5f, 0.0f, -6.0f)
    glRotatef(quadRotation, 1.0f, 1.0f, 1.0f)
    glVertexPointer(3, GL_FLOAT, 0, quadVertexBuffer)
    glColorPointer(4, GL_FLOAT, 0, quadColorBuffer)
    glDrawArrays(GL_TRIANGLES, 0, quadVertices.length / 3)
    glDisableClientState(GL_VERTEX_ARRAY)
    glDisableClientState(GL_COLOR_ARRAY)

    triangleRotation += 0.2f
    quadRotation += 0.15f
  }
}

object Lesson05 {
  def main(args: Array[String]): Unit = {
    val lesson05 = new Lesson05()
    val controller = LWJGLController(lesson05, 1024, 768, "NeHe Lesson 05")
  }
}
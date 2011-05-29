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

import org.sgine.opengl.GLDisplay
import org.sgine.opengl.GL._
import java.nio.{FloatBuffer, ByteOrder, ByteBuffer}

import org.sgine.opengl.lwjgl.LWJGLController

/**
 * Ported from the NeHe OpenGL Tutorials Lesson 02
 *
 * http://org.sgine.opengl.nehe.gamedev.net/data/lessons/lesson.asp?lesson=02
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Lesson02 extends GLDisplay {
  private val triangleVertices = Array(0.0f, 1.0f, 0.0f,
                                       -1.0f, -1.0f, 0.0f,
                                       1.0f, -1.0f, 0.0f)
  private var triangleVertexBuffer: FloatBuffer = _

  private val quadVertices = Array(-1.0f, -1.0f, 0.0f,
                                   1.0f, -1.0f, 0.0f,
                                   -1.0f, 1.0f, 0.0f,
                                   1.0f, 1.0f, 0.0f)
  private var quadVertexBuffer: FloatBuffer = _

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

    // Create Quad
    bb = ByteBuffer.allocateDirect(quadVertices.length * 4)
    bb.order(ByteOrder.nativeOrder())
    quadVertexBuffer = bb.asFloatBuffer()
    quadVertexBuffer.put(quadVertices)
    quadVertexBuffer.position(0)
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

    glVertexPointer(3, GL_FLOAT, 0, triangleVertexBuffer)
    glEnableClientState(GL_VERTEX_ARRAY)
    glDrawArrays(GL_TRIANGLE_STRIP, 0, triangleVertices.length / 3)
    glDisableClientState(GL_VERTEX_ARRAY)

    glTranslatef(3.0f, 0.0f, 0.0f)
    glVertexPointer(3, GL_FLOAT, 0, quadVertexBuffer)
    glEnableClientState(GL_VERTEX_ARRAY)
    glDrawArrays(GL_TRIANGLE_STRIP, 0, quadVertices.length / 3)
    glDisableClientState(GL_VERTEX_ARRAY)
  }
}

object Lesson02 {
  def main(args: Array[String]): Unit = {
    val lesson02 = new Lesson02()
    val controller = LWJGLController(lesson02, 1024, 768, "NeHe Lesson 02")
  }
}
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

package org.sgine.opengl.lwjgl

import org.sgine.opengl.GLDisplay
import org.sgine.opengl.GL._
import java.nio.{ShortBuffer, FloatBuffer, ByteOrder, ByteBuffer}

/**
*
*
* @author Matt Hicks <mhicks@sgine.org>
*/
class Test extends GLDisplay {
  private var indexBuffer: ShortBuffer = _
  private var vertexBuffer: FloatBuffer = _

  def create() = {
    println("CREATE!")

    glShadeModel(GL_SMOOTH)
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
//    glClearDepthf(1.0f)
    glEnable(GL_DEPTH_TEST)
    glDepthFunc(GL_LEQUAL)
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST)

//    glClearDepthf(1.0f)
//    glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
//    glEnable(GL_BLEND)
//    glEnable(GL_DEPTH_TEST)
//    glDepthFunc(GL_LEQUAL)
//    glEnable(GL_POLYGON_OFFSET_FILL)
//    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)
//    glEnable(GL_TEXTURE_2D)
//    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
//    glEnableClientState(GL_VERTEX_ARRAY)

    val vbb = ByteBuffer.allocateDirect(3 * 3 * 4)
    vbb.order(ByteOrder.nativeOrder())
    vertexBuffer = vbb.asFloatBuffer()

    // short has 2 bytes
    val ibb = ByteBuffer.allocateDirect(3 * 2)
    ibb.order(ByteOrder.nativeOrder())
    indexBuffer = ibb.asShortBuffer()

    val coords = Array(
        0.0f, 1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
        1.0f, -1.0f, 0.0f
//        -0.5f, -0.5f, 0f, // (x1, y1, z1)
//        0.5f, -0.5f, 0f, // (x2, y2, z2)
//        0f, 0.5f, 0f // (x3, y3, z3)
    )

//    GL_QUADS
//    -1.0f, 1.0f, 0.0f
//    1.0f, 1.0f, 0.0f
//    1.0f, -1.0f, 0.0f
//    -1.0f, -1.0f, 0.0f

    vertexBuffer.put(coords)
    indexBuffer.put(3.toShort)

    vertexBuffer.position(0)
    indexBuffer.position(0)
  }

  def pause() = {
    println("PAUSE!")
  }

  def resume() = {
    println("RESUME!")
  }

  def resize(width: Int, height: Int) = {
    println("RESIZE!")
    glViewport(0, 0, width, height)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()

	// Calculate The Aspect Ratio Of The Window
//	gluPerspective(45.0f,(GLfloat)width/(GLfloat)height,0.1f,100.0f);

    val h = 1024.0f / 768.0f
//              gluPerspective(45.0f, h, 100.0f, 20000.0f)
    glFrustum(-1.0f / h, 1.0f * h, -1.0f, 1.0f, 1.0f, 100.0f)
    
    glMatrixMode(GL_MODELVIEW);						// Select The Modelview Matrix
    glLoadIdentity();
  }

  def update() = {
//    println("UPDATE!")
  }

  def render() = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glLoadIdentity()
    glTranslatef(-1.5f, 0.0f, -6.0f)
    glColor4f(1.0f, 1.0f, 1.0f, 1.0f)

    glVertexPointer(3, GL_FLOAT, 0, vertexBuffer)
    glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_SHORT, indexBuffer)
    
//    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
//    glLoadIdentity()
//    glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
//
//    glVertexPointer(3, GL_FLOAT, 0, vertexBuffer)
//    glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_SHORT, indexBuffer)
//    println("RENDER!")
  }

  def dispose() = {
    println("DISPOSE!")
  }
}

object Test {
  def main(args: Array[String]): Unit = {
    val test = new Test()
    val controller = LWJGLController(test, 1024, 768, "Test")
  }
}
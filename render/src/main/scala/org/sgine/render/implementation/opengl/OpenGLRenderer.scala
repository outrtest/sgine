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

import org.sgine.math.Matrix4
import org.sgine.opengl.GL._
import org.sgine.opengl.GLDisplay
import org.sgine.render.{RenderApplication, MutableShape, Renderer}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class OpenGLRenderer(val application: RenderApplication) extends Renderer with GLDisplay {
  private val matrixBuffer = Matrix4.floatBuffer

  def loadMatrix(matrix: Matrix4) = {
    matrix.toBuffer(matrixBuffer)
    glLoadMatrix(matrixBuffer)
  }

  protected[render] def createShape(vertices: Seq[Float]) = {
    val shape = new OpenGLVBOShapeRenderer
    shape.updateVertices(vertices, false)
    shape
  }

  protected[render] def createMutableShape(vertices: Seq[Float]) = {
    val shape = new OpenGLVBOShapeRenderer with MutableShape
    shape.updateVertices(vertices, true)
    shape
  }

  override def create() = {
    glShadeModel(GL_SMOOTH)
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    glClearDepth(1.0f)
    glEnable(GL_DEPTH_TEST)
    glDepthFunc(GL_LEQUAL)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()

    super.create()
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

    application.update()
    application.render()
  }

  def dispose() = {
    application.dispose()
  }
}
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

import org.sgine.opengl.{GLController, GLDisplay}
import java.util.concurrent.atomic.AtomicBoolean
import org.lwjgl.opengl.{PixelFormat, Display}

import org.sgine.opengl.GL._
import java.awt.{BorderLayout, Frame, Canvas}
import java.awt.event.{FocusAdapter, ComponentEvent, ComponentAdapter}

class LWJGLController(val display: GLDisplay, val canvas: Canvas = new java.awt.Canvas) extends GLController {
  // Set to true when the AWT canvas has been resized
  private val resized = new AtomicBoolean()

  canvas.addComponentListener(new ComponentAdapter() {
    override def componentResized(evt: ComponentEvent) = {
      resized.set(true)
    }
  })

  def gl = GL

  def create() = {
    // TODO: resolve these
    Display.setFullscreen(false)
    Display.setVSyncEnabled(true)
    Display.setParent(canvas)

    val alpha = 0
    val depth = 8
    val stencil = 0
    val samples = 0
    val bpp = 0
    val auxBuffers = 0
    val accumBPP = 0
    val accumAlpha = 0
    val stereo = false
    val floatingPoint = false
    val format = new PixelFormat(bpp, alpha, depth, stencil, samples, auxBuffers, accumBPP, accumAlpha, stereo, floatingPoint)
    Display.create(format)
  }

  override def update() = {
    if (Display.isCloseRequested) {
      shutdown()
    }
    if (resized.compareAndSet(true, false)) {
      resize(canvas.getWidth, canvas.getHeight)
      display.resize(canvas.getWidth, canvas.getHeight)
    }
    Display.update()
  }

  def dispose() = {
    Display.destroy()
  }
}

object LWJGLController {
  def apply(display: GLDisplay, width: Int, height: Int, title: String) = {
    val controller = new LWJGLController(display)

    val frame = new Frame
    frame.setSize(width, height)
    frame.setTitle(title)
    frame.setResizable(false)
    frame.setLayout(new BorderLayout())
    frame.add(BorderLayout.CENTER, controller.canvas)
    frame.addWindowListener(new java.awt.event.WindowAdapter() {
			override def windowClosing(e: java.awt.event.WindowEvent) = {
				controller.shutdown()
				frame.dispose()
			}
		})

    frame.setVisible(true)
    controller.start()
  }
}
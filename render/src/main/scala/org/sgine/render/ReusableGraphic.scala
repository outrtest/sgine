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

import scala.math._
import java.awt.image.{DataBuffer, Raster, BufferedImage}
import java.awt.{Color, Graphics2D}
import java.awt.RenderingHints._
import java.util.Hashtable
import java.nio.ByteBuffer
import java.lang.ThreadLocal

/**
 * ReusableGraphics is backed by a java.awt.BufferedImage with locking support for multi-threaded safety.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
private class ReusableGraphic {
  private var bufferedImage: BufferedImage = null

  def apply(width: Int, height: Int, buffer: ByteBuffer)(draw: Graphics2D => Unit): Unit = {
    // Create new BufferedImage if necessary
    if (bufferedImage == null) {
      createBufferedImage(width, height)
    } else if (bufferedImage.getWidth < width || bufferedImage.getHeight < height) {
      createBufferedImage(max(width, bufferedImage.getWidth), max(height, bufferedImage.getHeight))
    }

    // Configure graphical context
    val g = bufferedImage.createGraphics()
    g.setBackground(ReusableGraphic.clearColor)
    g.clearRect(0, 0, bufferedImage.getWidth, bufferedImage.getHeight)
    // TODO: make the following more configurable
    g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY)
    g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
    g.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON)
    g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC)
    g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY)
    g.setRenderingHint(KEY_STROKE_CONTROL, VALUE_STROKE_PURE)

    // Apply function to draw
    draw(g)

    // Apply image to buffer
    ImageUtils(bufferedImage, 0, 0, width, height, buffer)
  }

  def clear() = {
    bufferedImage.flush()
    bufferedImage = null
  }

  private def createBufferedImage(width: Int, height: Int) = {
    val bands = 4
    val premultiplied = false
    val raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, bands, null)
    val properties = new Hashtable[String, Object]()
    properties.put("reusableGraphic", "yes")
    bufferedImage = new BufferedImage(ImageUtils.RGBA, raster, premultiplied, properties)
  }
}

object ReusableGraphic {
  private val instance = new ThreadLocal[ReusableGraphic]
  private val clearColor = new Color(0.0f, 0.0f, 0.0f, 0.0f)

  /**
   * Reuses or replaces backing BufferedImage and applies <code>draw</code> to the underlying graphics context.
   *
   * Contents of image are applied to <code>buffer</code>.
   *
   * Note that this method will block until it can lock this instance of ReusableGraphic.
   */
  def apply(width: Int, height: Int, buffer: ByteBuffer)(draw: Graphics2D => Unit): Unit = {
    apply()(width, height, buffer)(draw)
  }

  private def apply() = {
    instance.get() match {
      case null => {
        val rg = new ReusableGraphic()
        instance.set(rg)
        rg
      }
      case rg => rg
    }
  }

  /**
   * Clears out the backed BufferedImage to make contents available for GC.
   *
   * Note that this method will block until it can lock this instance of ReusableGraphic.
   */
  def clear() = apply().clear()
}
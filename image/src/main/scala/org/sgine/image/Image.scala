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

package org.sgine.image

import de.matthiasmann.twl.utils.PNGDecoder
import java.net.URL
import java.nio.{ByteOrder, ByteBuffer}
import java.awt.color.ColorSpace
import java.awt.image.{DataBuffer, ComponentColorModel, BufferedImage}
import java.lang.IllegalArgumentException
import java.awt.{AlphaComposite, Graphics2D, Transparency}

/**
 * Image provides a general purpose class to represent an image in memory and provides many useful features to load and
 * manipulate.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image private(val width: Int, val height: Int, val buffer: ByteBuffer) {
  /**
   * Load an image via a URL.
   *
   * Note that the loaded image must represent the exact width and height specified for this image or an
   * IllegalArgumentException will be thrown.
   */
  def load(url: URL) = {
    val input = url.openStream
		try {
			val decoder = new PNGDecoder(input)
      if (decoder.getWidth != width || decoder.getHeight != height) {
        throw new IllegalArgumentException("Invalid width and height values. Actual: " + decoder.getWidth + "x" + decoder.getHeight + ", Supplied: " + width + "x" + height)
      }
			decoder.decode(buffer, decoder.getWidth * 4, PNGDecoder.Format.RGBA)
			buffer.flip()
		} finally {
			input.close()
		}
  }

  /**
   * Loads the buffered image passed in. Only loads data for the width and height specified in the Image.
   */
  def load(bufferedImage: BufferedImage, x: Int = 0, y: Int = 0) = {
    if (bufferedImage.getColorModel == Image.RGBA) {    // Image is compatible, apply it
      if (bufferedImage.getProperty("reusableGraphic") != "yes") {    // Work-around for ReusableGraphic
        bufferedImage.coerceData(true)    // Make sure the data is compatible
      }

      val data = new Array[Byte](width * 4)
      val raster = bufferedImage.getRaster
      for (i <- 0 until height) {
        raster.getDataElements(x, y + i, width, 1, data)
        buffer.put(data)
      }
      buffer.flip()
    } else {                                            // Image needs to be converted - use ReusableGraphic
      val draw = (g: Graphics2D) => {
        g.setComposite(AlphaComposite.Src)
        g.drawImage(bufferedImage, 0, 0, width, height, null)
        g.dispose()
      }
      ReusableGraphic(this)(draw)
    }
  }
}

object Image {
  val RGBA = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			Array[Int](8, 8, 8, 8),
			true,
			false,
			Transparency.TRANSLUCENT,
			DataBuffer.TYPE_BYTE)

  def apply(width: Int, height: Int) = {
    val buffer = Image.createBuffer(width, height)
    new Image(width, height, buffer)
  }

  def apply(width: Int, height: Int, buffer: ByteBuffer) = new Image(width, height, buffer)

  def apply(url: URL) = {
    val input = url.openStream
		try {
			val decoder = new PNGDecoder(input)
      val buffer = createBuffer(decoder.getWidth, decoder.getHeight)
			decoder.decode(buffer, decoder.getWidth * 4, PNGDecoder.Format.RGBA)
			buffer.flip()
      
      new Image(decoder.getWidth, decoder.getHeight, buffer)
		} finally {
			input.close()
		}
  }

  def createBuffer(width: Int, height: Int) = {
    ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder)
  }
}
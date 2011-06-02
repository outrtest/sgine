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
import java.awt.Transparency
import java.awt.image.{DataBuffer, ComponentColorModel, BufferedImage}

/**
 * 
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
case class Image(width: Int, height: Int, buffer: ByteBuffer)

object Image {
  val RGBA = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			Array[Int](8, 8, 8, 8),
			true,
			false,
			Transparency.TRANSLUCENT,
			DataBuffer.TYPE_BYTE)

  def load(bufferedImage: BufferedImage, x: Int, y: Int, width: Int, height: Int) = {
    if (bufferedImage.getColorModel == RGBA) {
      if (bufferedImage.getProperty("reusableGraphic") != "yes") {    // Work-around for ReusableGraphic
        bufferedImage.coerceData(true)    // Make sure the data is compatible
      }

      val data = new Array[Byte](width * 4)
      val raster = bufferedImage.getRaster
      val buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder)
      for (i <- 0 until height) {
        raster.getDataElements(x, y + i, width, 1, data)
        buffer.put(data)
      }
      buffer.flip()

      Image(width, height, buffer)
    } else {
      // TODO: add support for ReusableGraphic
    }
  }

  def load(url: URL) = {
    val input = url.openStream
		try {
			val decoder = new PNGDecoder(input)
      // TODO: support buffer re-use
			val buffer = ByteBuffer.allocateDirect((decoder.getWidth * decoder.getHeight) * 4).order(ByteOrder.nativeOrder)
			decoder.decode(buffer, decoder.getWidth * 4, PNGDecoder.Format.RGBA)
			buffer.flip()

			Image(decoder.getWidth, decoder.getHeight, buffer)
		} finally {
			input.close()
		}
  }
}
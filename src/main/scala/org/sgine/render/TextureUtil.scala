package org.sgine.render

import java.awt.AlphaComposite
import java.awt.image.BufferedImage

import java.net.URL

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

import org.sgine.util.GeneralReusableGraphic
import org.sgine.util.ReusableGraphic

object TextureUtil {
	def apply(image: BufferedImage): Texture = apply(image, image.getWidth, image.getHeight)
	
	def apply(image: BufferedImage, width: Int, height: Int): Texture = {
		val texture = new BasicTexture(width, height)
		
		apply(texture, image, 0, 0, texture.width, texture.height)
		
		texture
	}
	
	/**
	 * Update a texture with the passed BufferedImage. This is a thread-safe operation.
	 * 
	 * @param texture
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param offsetX
	 * @param offsetY
	 */
	def apply(texture: BasicTexture, image: BufferedImage, x: Int, y: Int, width: Int, height: Int, offsetX: Int = 0, offsetY: Int = 0, buffer: ByteBuffer = null): Unit = {
		if (isValidImage(image)) {
			if (image.getProperty("reusableGraphic") != "yes") {
				image.coerceData(true)		// Make sure the data is compatible
			}
			
			val data = new Array[Byte](width * 4)
			val raster = image.getRaster
			val b = buffer match {
				case null => ByteBuffer.allocateDirect((width * height) * 4).order(ByteOrder.nativeOrder)
				case _ => buffer
			}
			
			for (i <- 0 until height) {
				raster.getDataElements(x, y + i, width, 1, data)
				buffer.put(data)
			}
			buffer.flip()
			
			texture(offsetX, offsetY, width, height, buffer)
		} else {
			val rg = GeneralReusableGraphic
			val g = rg(image.getWidth, image.getHeight, -1)
			try {
				g.setComposite(AlphaComposite.Src)
				g.drawImage(image, 0, 0, image.getWidth, image.getHeight, null)
				g.dispose()
				
				apply(texture, rg(), x, y, width, height, offsetX, offsetY)
			} finally {
				rg.release()
			}
		}
	}
	
	def image2Buffer(image: BufferedImage, buffer: ByteBuffer): Unit = {
		if (isValidImage(image)) {
			if (image.getProperty("reusableGraphic") != "yes") {
				image.coerceData(true)		// Make data compatible
			}
			
			val data = new Array[Byte](image.getWidth * 4)
			val raster = image.getRaster
			for (i <- 0 until image.getHeight) {
				raster.getDataElements(0, i, image.getWidth, 1, data)
				buffer.put(data)
			}
			buffer.flip()
		} else {
			val rg = GeneralReusableGraphic
			val g = rg(image.getWidth, image.getHeight, -1)
			try {
				g.setComposite(AlphaComposite.Src)
				g.drawImage(image, 0, 0, image.getWidth, image.getHeight, null)
				g.dispose()
				
				image2Buffer(rg(), buffer)
			} finally {
				rg.release()
			}
		}
	}
	
	/**
	 * Verify the color model of the image is directly compatible
	 * 
	 * @param image
	 * @return
	 * 		Boolean
	 */
	private def isValidImage(image: BufferedImage) = image.getColorModel() == ReusableGraphic.rgba
}
package org.sgine.render

import java.awt.AlphaComposite
import java.awt.image.BufferedImage

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL14._
import org.lwjgl.opengl.GLContext

import org.sgine.property.AdvancedProperty
import org.sgine.util.GeneralReusableGraphic
import org.sgine.util.ReusableGraphic

/**
 * Represents a texture in the renderer.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Texture {
	lazy val id = generateId()
	var mipmap: Boolean = true
	
	private var _width: Int = _
	private var _height: Int = _
	
	def width = _width
	def height = _height
	
	private var update: TextureUpdate = _
	
	/**
	 * Generates the texture id
	 * 
	 * @return
	 * 		textureId
	 */
	private def generateId() = {
		val tmp = IntBuffer.allocate(1);
		glGenTextures(tmp);
		tmp.get(0);
	}
	
	/**
	 * Bind the texture to be used. Must be called within the OpenGL thread.
	 */
	def bind() = {
		glBindTexture(GL_TEXTURE_2D, id)
		
		validateTexture()
		
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
	}
	
	/**
	 * Update the texture with the passed ByteBuffer. This is a thread-safe operation.
	 * 
	 * @param width
	 * @param height
	 * @param buffer
	 * @param textureFormat
	 * @param imageFormat
	 * @param imageType
	 */
	def apply(width: Int, height: Int, buffer: ByteBuffer, textureFormat: Int = GL_RGBA, imageFormat: Int = GL_RGBA, imageType: Int = GL_UNSIGNED_BYTE): Unit = {
		update = new TextureUpdate(width, height, buffer, textureFormat, imageFormat, imageType)
	}
	
	/**
	 * Update the texture with the passed BufferedImage. This is a thread-safe operation.
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	def apply(image: BufferedImage, x: Int, y: Int, width: Int, height: Int): Unit = {
		if (isValidImage(image)) {
			if (image.getProperty("reusableGraphic") != "yes") {
				image.coerceData(true)		// Make sure the data is compatible
			}
			
			val data = new Array[Byte](width * 4)
			val raster = image.getRaster
			val buffer = ByteBuffer.allocateDirect((width * height) * 4).order(ByteOrder.nativeOrder)
			
			for (i <- 0 until height) {
				raster.getDataElements(x, y + i, width, 1, data)
				buffer.put(data)
			}
			buffer.flip()
			
			apply(width, height, buffer)
		} else {
			val rg = GeneralReusableGraphic
			val g = rg(image.getWidth, image.getHeight, -1)
			try {
				g.setComposite(AlphaComposite.Src)
				g.drawImage(image, 0, 0, image.getWidth, image.getHeight, null)
				g.dispose()
				
				apply(rg(), x, y, width, height)
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
	
	/**
	 * Verifies and updates the texture as necessary
	 */
	private def validateTexture() = {
		val u = update
		update = null
		
		if (u != null) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, if (mipmap) GL_LINEAR_MIPMAP_LINEAR else GL_LINEAR)
			if (GLContext.getCapabilities.OpenGL12) {		// Only available in 1.2+
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
			}
			if ((mipmap) && (GLContext.getCapabilities.GL_SGIS_generate_mipmap)) {		// Use mipmapping, it's supported and requested
				glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE)
			}
			var x = 0
			var y = 0
			var w = u.width
			var h = u.height
			if ((u.width != width) || (u.height != height)) {
				if (GLContext.getCapabilities.GL_ARB_texture_non_power_of_two) {
					x = 0
					y = 0
				} else {		// NPOT not supported, need to adjust
					w = nextPOT(w)
					h = nextPOT(h)
					x = Math.round((w - u.width) / 2.0f)
					y = Math.round((h - u.height) / 2.0f)
				}
				glTexImage2D(GL_TEXTURE_2D, 0, u.imageFormat, w, h, 0, u.textureFormat, u.imageType, null.asInstanceOf[ByteBuffer])
				
				_width = u.width
				_height = u.height
			}
			glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, u.width, u.height, u.textureFormat, u.imageType, u.buffer)
		}
	}
	
	/**
	 * The next power-of-two value
	 * 
	 * @param value
	 * @return
	 * 		Int
	 */
	private def nextPOT(value: Int) = {
		var ret = 1
		while (ret < value) {
			ret <<= 1
		}
		ret
	}
}

case class TextureUpdate(width: Int, height: Int, buffer: ByteBuffer, textureFormat: Int = GL_RGBA, imageFormat: Int = GL_RGBA, imageType: Int = GL_UNSIGNED_BYTE)
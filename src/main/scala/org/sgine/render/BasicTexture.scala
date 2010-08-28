package org.sgine.render

import java.nio.ByteBuffer
import java.nio.IntBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL14._
import org.lwjgl.opengl.GLContext

import org.sgine.property.AdvancedProperty

import scala.math._

/**
 * Represents a texture in the renderer.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class BasicTexture() extends Texture {
	def width = _width
	def height = _height
	
	private var _width = 0
	private var _height = 0
	
	def this(width: Int, height: Int) = {
		this()
		_width = width
		_height = height
	}
	
	lazy val id = generateId()
	var mipmap: Boolean = true
	
	private val updates = new java.util.concurrent.ArrayBlockingQueue[TextureUpdate](10)
	
	/**
	 * Generates the texture id
	 * 
	 * @return
	 * 		textureId
	 */
	private def generateId() = glGenTextures()
	
	def update() = {
		if (updates.size > 0) {
			bind()
		}
	}
	
	def bind() = {
		if (Texture.current != id) {
			glBindTexture(GL_TEXTURE_2D, id)
			
			validateTexture()
			
			glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
			
			Texture.current = id
		}
	}
	
	/**
	 * Update the texture with the passed ByteBuffer. This is a thread-safe operation.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param buffer
	 * @param textureFormat
	 * @param imageFormat
	 * @param imageType
	 */
	def apply(x: Int, y: Int, width: Int, height: Int, buffer: ByteBuffer, textureFormat: Int = GL_RGBA, imageFormat: Int = GL_COMPRESSED_RGBA, imageType: Int = GL_UNSIGNED_BYTE): Unit = {
		updates.add(new TextureUpdate(x, y, width, height, buffer, textureFormat, imageFormat, imageType))
	}
	
	private var created = false
	
	/**
	 * Verifies and updates the texture as necessary
	 */
	private def validateTexture() = {
		val u = updates.poll()

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
			var x = u.x
			var y = u.y
			var w = u.width
			var h = u.height
			if ((u.width != width) || (u.height != height)) {
				if (GLContext.getCapabilities.GL_ARB_texture_non_power_of_two) {
					x = 0
					y = 0
				} else {		// NPOT not supported, need to adjust
					w = nextPOT(w)
					h = nextPOT(h)
					x = round((w - u.width) / 2.0f)
					y = round((h - u.height) / 2.0f)
				}
				_width = w
				_height = h
			}
			if (!created) {
				glTexImage2D(GL_TEXTURE_2D, 0, u.imageFormat, w, h, 0, u.textureFormat, u.imageType, null.asInstanceOf[ByteBuffer])
				created = true
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
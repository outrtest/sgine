package org.sgine.render

import java.util.concurrent.atomic.AtomicBoolean

import java.nio.ByteBuffer
import java.nio.ByteOrder

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL14._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL21._
import org.lwjgl.opengl.GL30._

import org.sgine.work.DefaultWorkManager

class StreamingTexture(val width: Int, val height: Int) extends Texture {
	private var _id: Int = _
	private var _bufferId: Int = _
	private var _buffer: ByteBuffer = _
	private lazy val length = width * height * textureDepth
	
	var internalTextureFormat = GL_COMPRESSED_RGBA
	var pixelTextureFormat = GL_COMPRESSED_RGBA
	var textureType = GL_UNSIGNED_BYTE
	var textureDepth = 4

	def id = _id
	var updateFunction: (ByteBuffer) => Unit = null
	var asynchronous = false
	
	private var initialized = false
	private var currentBufferIndex = 0
	private val dataDirty = new AtomicBoolean(false)
	private val updatingBuffer = new AtomicBoolean(false)
	private val bufferDirty = new AtomicBoolean(false)
	
	private def init() = {
		_id = glGenTextures()
		_bufferId = glGenBuffers()
		glBindBuffer(GL_PIXEL_UNPACK_BUFFER, _bufferId)
		glBufferData(GL_PIXEL_UNPACK_BUFFER, length, GL_STREAM_DRAW)
		
		glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
		
		// Generate the a blank texture and set up
		glBindTexture(GL_TEXTURE_2D, id)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
		glTexImage2D(GL_TEXTURE_2D, 0, internalTextureFormat, width, height, 0, pixelTextureFormat, textureType, null.asInstanceOf[ByteBuffer])
	}
	
	def update() = {
		if ((dataDirty.get) || (bufferDirty.get)) {
			bind()
		}
	}
	
	def bind() = {
		if (Texture.current != id) {
			if (!initialized) {
				init()
				initialized = true
			}
	
			glBindTexture(GL_TEXTURE_2D, id)
			glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE)
	
			if (dataDirty.compareAndSet(true, false)) {
				updateBuffer()
			}
			if (bufferDirty.compareAndSet(true, false)) {
				applyBuffer()
			}
			
			Texture.current = id
		}
	}
	
	def invalidateTexture() = dataDirty.set(true)

	// Called externally to update texture buffer
	private def updateBuffer() = {
		if (updatingBuffer.compareAndSet(false, true)) {
			glBindBuffer(GL_PIXEL_UNPACK_BUFFER, _bufferId)
			val length = width * height * textureDepth
			_buffer = glMapBufferRange(GL_PIXEL_UNPACK_BUFFER, 0, length, GL_MAP_WRITE_BIT | GL_MAP_INVALIDATE_BUFFER_BIT, _buffer)
			_buffer.order(ByteOrder.nativeOrder()).clear()
			glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
			
			if (asynchronous) {
				DefaultWorkManager += updateBufferFunction
			} else {
				updateBufferFunction()
			}
		}
	}
	
	// Asynchronous function that updates the buffer
	private val updateBufferFunction = () => {
		// Update texture
		updateFunction(_buffer)
		
		// Let the renderer know the buffer is dirty
		bufferDirty.set(true)
	}
	
	// Final stage of re-applying buffer data to texture
	private def applyBuffer() = {
		glBindBuffer(GL_PIXEL_UNPACK_BUFFER, _bufferId)
		glUnmapBuffer(GL_PIXEL_UNPACK_BUFFER)
		glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, pixelTextureFormat, textureType, 0)
		glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
		
		// Remove lock on updates
		updatingBuffer.set(false)
	}
}
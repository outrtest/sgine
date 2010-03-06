package org.sgine.render.lwjgl

import org.sgine.core.Color
import org.sgine.math.VertexBuffer

import org.lwjgl.opengl.GL11._

object LWJGLRenderContext {
	private var currentShape: Int = -1
	
	private var texture: LWJGLTexture = _
	private var coordinatesBuffer = VertexBuffer(2, 6)
	
	private val tc2d = glTexCoord2d _
	
	def setColor(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 1.0): Unit = glColor4d(red, green, blue, alpha)
	
	def setColor(color: Color): Unit = setColor(color.red, color.green, color.blue, color.alpha)
	
	def setTexture(texture: LWJGLTexture) = {
		if (texture != null) texture.bind()
		this.texture = texture
	}
	
	def translate(x: Double, y: Double, z: Double) = glTranslated(x, y, z)
	
	def scale(x: Double = 1.0, y: Double = 1.0, z: Double = 1.0) = glScaled(x, y, z)
	
	def texCoord(index: Int, s: Double, t: Double) = {
		coordinatesBuffer = ensureVertexBufferLength(coordinatesBuffer, index)
		coordinatesBuffer.set(0, index, s)
		coordinatesBuffer.set(1, index, t)
	}
	
	def drawRect(x: Double, y: Double, width: Double, height: Double) = {
		if (currentShape != GL_QUADS) {
			flush()
			glBegin(GL_QUADS)
		}

		if (texture != null) coordinatesBuffer(0, tc2d)
		glVertex3d(x, y, 0.0)
		if (texture != null) coordinatesBuffer(1, tc2d)
		glVertex3d(x + width, y, 0.0)
		if (texture != null) coordinatesBuffer(2, tc2d)
		glVertex3d(x + width, y + height, 0.0)
		if (texture != null) coordinatesBuffer(3, tc2d)
		glVertex3d(x, y + height, 0.0)
	}
	
	def drawImage(image: LWJGLImage, x: Double, y: Double, width: Double, height: Double) = {
		if (texture != image.texture) {
			setTexture(image.texture)
		}
		
		val ix = image.x / image.texture.width
		val iy = image.y / image.texture.height
		val iw = ix + (image.width / image.texture.width)
		val ih = iy + (image.height / image.texture.height)
		
		if (currentShape != GL_QUADS) {
			flush()
			glBegin(GL_QUADS)
		}
		
		glTexCoord2d(ix, ih)
		glVertex3d(x, y, 0.0)
		glTexCoord2d(iw, ih)
		glVertex3d(x + width, y, 0.0)
		glTexCoord2d(iw, iy)
		glVertex3d(x + width, y + height, 0.0)
		glTexCoord2d(ix, iy)
		glVertex3d(x, y + height, 0.0)
	}
	
	def flush() = {
		if (currentShape != -1) {
			glEnd()
			currentShape = -1
		}
	}
	
	private def ensureVertexBufferLength(buffer: VertexBuffer, length: Int): VertexBuffer = {
		if (buffer.length <= length) {
			buffer.clone(length * 2)
		} else {
			buffer
		}
	}
}
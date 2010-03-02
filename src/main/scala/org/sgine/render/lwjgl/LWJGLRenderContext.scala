package org.sgine.render.lwjgl

import org.sgine.math.VertexBuffer

import org.lwjgl.opengl.GL11._

object LWJGLRenderContext {
	private var currentShape: Int = -1
	
	private var coordinatesBuffer = VertexBuffer(2, 6)
	
	private val tc2d = glTexCoord2d _
	
	def setColor(red: Double = 0.0, green: Double = 0.0, blue: Double = 0.0, alpha: Double = 1.0) = glColor4d(red, green, blue, alpha)
	
	def translate(x: Double, y: Double, z: Double) = glTranslated(x, y, z)
	
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

		coordinatesBuffer(0, tc2d)
		glVertex3d(x, y, 0.0)
		coordinatesBuffer(1, tc2d)
		glVertex3d(x + width, y, 0.0)
		coordinatesBuffer(2, tc2d)
		glVertex3d(x + width, y + height, 0.0)
		coordinatesBuffer(3, tc2d)
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
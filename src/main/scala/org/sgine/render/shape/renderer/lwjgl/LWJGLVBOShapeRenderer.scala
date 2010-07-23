package org.sgine.render.shape.renderer.lwjgl

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._

import org.sgine.core.Face

import org.sgine.render.shape.ShapeData

class LWJGLVBOShapeRenderer extends LWJGLShapeRenderer {
	private var vbo: VBO = _
	
	protected[shape] def update(old: ShapeData, data: ShapeData) = {
		if (old != null) {
			if (old.length != data.length) {
				// Delete buffer if already existing since the size changed
				vbo.delete()
				vbo = null
			}
		}
		if (vbo == null) {
			vbo = new VBO()
		}
		vbo.update(data)
	}
	
	override protected[shape] def render(data: ShapeData) = {
		super.render(data)
		
		vbo.draw()
	}
}

object LWJGLVBOShapeRenderer {
	def capable() = org.lwjgl.opengl.GLContext.getCapabilities.OpenGL15 && org.lwjgl.opengl.GLContext.getCapabilities.GL_ARB_vertex_buffer_object
}

class VBO() {
	private var id: Int = glGenBuffers()
	private var data: ShapeData = _
	private var bb: ByteBuffer = _
	private var fb: FloatBuffer = _
	
	def update(data: ShapeData) = {
		// Bind the buffer
		glBindBuffer(GL_ARRAY_BUFFER, id)
		
		// Generate the buffer allocation the first time around
		if (bb == null) {
			val bytes = data.bytes
			glBufferData(GL_ARRAY_BUFFER, bytes, GL_STREAM_DRAW)
		}
		
		// Get the buffer to update
		val buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, bb)
		if (buffer != bb) {
			bb = buffer
			fb = buffer.order(ByteOrder.nativeOrder).asFloatBuffer
		}
		
		// Update the FloatBuffer
		val range = 0 until data.length
		
		fb.clear()
		
		for (i <- range) {	// Populate vertices
			val v = data.vertex(i)
			fb.put(v.x.toFloat)
			fb.put(v.y.toFloat)
			fb.put(v.z.toFloat)
		}
		
		if (data.hasNormal) {
			for (i <- range) {	// Populate normals
				val n = data.normal(i)
				fb.put(n.x.toFloat)
				fb.put(n.y.toFloat)
				fb.put(n.z.toFloat)
			}
		}
		
		if (data.hasColor) {
			for (i <- range) {	// Populate colors
				val c = data.color(i)
				fb.put(c.red.toFloat)
				fb.put(c.green.toFloat)
				fb.put(c.blue.toFloat)
				fb.put(c.alpha.toFloat)
			}
		}
		
		if (data.hasTexture) {
			for (i <- range) {	// Populate texture
				val t = data.texture(i)
				fb.put(t.x.toFloat)
				fb.put(t.y.toFloat)
			}
		}
		
		// Unmap the buffer
		glUnmapBuffer(GL_ARRAY_BUFFER)
		
		// Assign data data
		this.data = data
	}
	
	def draw() = {
		glEnableClientState(GL_VERTEX_ARRAY)
		glBindBuffer(GL_ARRAY_BUFFER, id)
		
		val length = data.length
		var offset = 0
		glVertexPointer(3, GL_FLOAT, 0, offset)
		offset += length * (3 * 4)
		if (data.hasNormal) {
			glEnableClientState(GL_NORMAL_ARRAY)
			glNormalPointer(GL_FLOAT, 0, offset)
			offset += length * (3 * 4)
		}
		if (data.hasColor) {
			glEnableClientState(GL_COLOR_ARRAY)
			glColorPointer(4, GL_FLOAT, 0, offset)
			offset += length * (4 * 4)
		}
		if (data.hasTexture) {
			glEnableClientState(GL_TEXTURE_COORD_ARRAY)
			glTexCoordPointer(2, GL_FLOAT, 0, offset)
		}
		glDrawArrays(data.mode, 0, length)
		
		glDisableClientState(GL_VERTEX_ARRAY)
		if (data.hasColor) {
			glDisableClientState(GL_COLOR_ARRAY)
		}
		if (data.hasTexture) {
			glDisableClientState(GL_TEXTURE_COORD_ARRAY)
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, 0)
	}
	
	def delete() = glDeleteBuffers(id)
}
package org.sgine.render.shape.renderer.lwjgl

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._

import org.sgine.core.Face

import org.sgine.render.shape._

class LWJGLVBOShapeRenderer extends LWJGLShapeRenderer {
	private var vbo: VBO = _
	
	protected[shape] def update(shape: Shape, vertexChanged: Boolean = false, colorChanged: Boolean = false, textureChanged: Boolean = false, normalChanged: Boolean = false) = {
		if ((vbo != null) && (vbo.length != shape.vertex.length)) {
			// Delete buffer if already existing since the size changed
			vbo.delete()
			vbo = null
		}
		if (shape.vertex != null) {
			if (vbo == null) {
				vbo = new VBO(shape.vertex.length)
			}
			vbo.update(shape, vertexChanged, colorChanged, textureChanged, normalChanged)
		}
	}
	
	override protected[shape] def renderInternal(shape: Shape) = if (vbo != null) vbo.draw()
}

object LWJGLVBOShapeRenderer {
	private[lwjgl] var current: Int = 0
	
	lazy val capable = org.lwjgl.opengl.GLContext.getCapabilities.OpenGL15 && org.lwjgl.opengl.GLContext.getCapabilities.GL_ARB_vertex_buffer_object
}

class VBO(val length: Int) {
	private var id: Int = glGenBuffers()
	private var shape: Shape = _
	private var bb: ByteBuffer = _
	private var fb: FloatBuffer = _
	
	def update(shape: Shape, vertexChanged: Boolean, colorChanged: Boolean, textureChanged: Boolean, normalChanged: Boolean) = {
		if (shape.vertex.length == 0) {
			throw new RuntimeException("Shape must have vertex data!")
		}
		
		// Bind the buffer
		glBindBuffer(GL_ARRAY_BUFFER, id)
		
		// Generate the buffer allocation the first time around
		if (bb == null) {
			val bytes = shape.bytes
			glBufferData(GL_ARRAY_BUFFER, bytes, GL_STREAM_DRAW)
		}
		
		// Get the buffer to update
		val buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, bb)
		if (buffer != bb) {
			bb = buffer
			fb = buffer.order(ByteOrder.nativeOrder).asFloatBuffer
		}
		
		// Update the FloatBuffer
		val range = 0 until shape.vertex.length
		
		fb.clear()
		
		for (i <- range) {	// Populate vertices
			val v = shape.vertex(i)
			fb.put(v.x.toFloat)
			fb.put(v.y.toFloat)
			fb.put(v.z.toFloat)
		}
		
		if ((normalChanged) && (shape.hasNormal)) {
			for (i <- range) {	// Populate normals
				val n = shape.normal(i)
				fb.put(n.x.toFloat)
				fb.put(n.y.toFloat)
				fb.put(n.z.toFloat)
			}
		}
		
		if ((colorChanged) && (shape.hasColor)) {
			for (i <- range) {	// Populate colors
				val c = shape.color(i)
				fb.put(c.red.toFloat)
				fb.put(c.green.toFloat)
				fb.put(c.blue.toFloat)
				fb.put(c.alpha.toFloat)
			}
		}
		
		if ((textureChanged) && (shape.hasTexture)) {
			for (i <- range) {	// Populate texture
				val t = shape.texture(i)
				fb.put(t.x.toFloat)
				fb.put(t.y.toFloat)
			}
		}
		
		// Unmap the buffer
		glUnmapBuffer(GL_ARRAY_BUFFER)
		
		// Assign shape reference
		this.shape = shape
	}
	
	def draw() = {
		val length = shape.vertex.length
		if (LWJGLVBOShapeRenderer.current != id) {
			LWJGLVBOShapeRenderer.current = id
			
			glEnableClientState(GL_VERTEX_ARRAY)
			glBindBuffer(GL_ARRAY_BUFFER, id)
			
			var offset = 0
			glVertexPointer(3, GL_FLOAT, 0, offset)
			offset += length * (3 * 4)
			if (shape.hasNormal) {
				glEnableClientState(GL_NORMAL_ARRAY)
				glNormalPointer(GL_FLOAT, 0, offset)
				offset += length * (3 * 4)
			}
			if (shape.hasColor) {
				glEnableClientState(GL_COLOR_ARRAY)
				glColorPointer(4, GL_FLOAT, 0, offset)
				offset += length * (4 * 4)
			}
			if (shape.hasTexture) {
				glEnableClientState(GL_TEXTURE_COORD_ARRAY)
				glTexCoordPointer(2, GL_FLOAT, 0, offset)
			}
		}
		glDrawArrays(shape.mode.value, 0, length)
		
//		glDisableClientState(GL_VERTEX_ARRAY)
//		if (data.hasColor) {
//			glDisableClientState(GL_COLOR_ARRAY)
//		}
//		if (data.hasTexture) {
//			glDisableClientState(GL_TEXTURE_COORD_ARRAY)
//		}
		
//		glBindBuffer(GL_ARRAY_BUFFER, 0)
	}
	
	def delete() = glDeleteBuffers(id)
}
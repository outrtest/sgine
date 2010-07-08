package org.sgine.render.spatial.renderer

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._

import org.sgine.render.spatial.MeshData

class VBOSpatialRenderer extends SpatialRenderer {
	private var vbo: VBO = _
	
	protected[spatial] def update(old: MeshData, mesh: MeshData) = {
		if (old != null) {
			if (old.length != mesh.length) {
				// Delete buffer if already existing since the size changed
				vbo.delete()
				vbo = null
			}
		}
		if (vbo == null) {
			vbo = new VBO()
		}
		vbo.update(mesh)
	}
	
	protected[spatial] def render(mesh: MeshData) = {
		vbo.draw()
	}
}

object VBOSpatialRenderer {
	def capable() = org.lwjgl.opengl.GLContext.getCapabilities.OpenGL15 && org.lwjgl.opengl.GLContext.getCapabilities.GL_ARB_vertex_buffer_object
}

class VBO() {
	private var id: Int = glGenBuffers()
	private var mesh: MeshData = _
	private var bb: ByteBuffer = _
	private var fb: FloatBuffer = _
	
	def update(mesh: MeshData) = {
		// Bind the buffer
		glBindBuffer(GL_ARRAY_BUFFER, id)
		
		// Generate the buffer allocation the first time around
		if (bb == null) {
			val bytes = mesh.bytes
			glBufferData(GL_ARRAY_BUFFER, bytes, GL_STREAM_DRAW)
		}
		
		// Get the buffer to update
		val buffer = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, bb)
		if (buffer != bb) {
			bb = buffer
			fb = buffer.order(ByteOrder.nativeOrder).asFloatBuffer
		}
		
		// Update the FloatBuffer
		val range = 0 until mesh.length
		
		fb.clear()
		
		for (i <- range) {	// Populate vertices
			val v = mesh.vertex(i)
			fb.put(v.x.toFloat)
			fb.put(v.y.toFloat)
			fb.put(v.z.toFloat)
		}
		
		if (mesh.hasColor) {
			for (i <- range) {	// Populate colors
				val c = mesh.color(i)
				fb.put(c.red.toFloat)
				fb.put(c.green.toFloat)
				fb.put(c.blue.toFloat)
				fb.put(c.alpha.toFloat)
			}
		}
		
		if (mesh.hasTexture) {
			for (i <- range) {	// Populate texture
				val t = mesh.texture(i)
				fb.put(t.x.toFloat)
				fb.put(t.y.toFloat)
			}
		}
		
		// Unmap the buffer
		glUnmapBuffer(GL_ARRAY_BUFFER)
		
		// Assign mesh data
		this.mesh = mesh
	}
	
	def draw() = {
		glEnableClientState(GL_VERTEX_ARRAY)
		glBindBuffer(GL_ARRAY_BUFFER, id)
		
		val length = mesh.length
		var offset = 0
		glVertexPointer(3, GL_FLOAT, 0, offset)
		offset += length * (3 * 4)
		if (mesh.hasColor) {
			glEnableClientState(GL_COLOR_ARRAY)
			glColorPointer(4, GL_FLOAT, 0, offset)
			offset += length * (4 * 4)
		}
		if (mesh.hasTexture) {
			glEnableClientState(GL_TEXTURE_COORD_ARRAY)
			glTexCoordPointer(2, GL_FLOAT, 0, offset)
		}
		glDrawArrays(mesh.mode, 0, length)
		
		glDisableClientState(GL_VERTEX_ARRAY)
		if (mesh.hasColor) {
			glDisableClientState(GL_COLOR_ARRAY)
		}
		if (mesh.hasTexture) {
			glDisableClientState(GL_TEXTURE_COORD_ARRAY)
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, 0)
	}
	
	def delete() = glDeleteBuffers(id)
}
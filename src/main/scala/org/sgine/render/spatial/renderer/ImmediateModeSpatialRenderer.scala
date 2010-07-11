package org.sgine.render.spatial.renderer

import org.lwjgl.opengl.GL11._

import org.sgine.render.spatial.MeshData

class ImmediateModeSpatialRenderer extends LWJGLSpatialRenderer {
	private var range: Range = _
	private var mesh: MeshData = _
	
	protected[spatial] def update(old: MeshData, mesh: MeshData) = {
		// Immediate mode, nothing to do
	}
	
	override protected[spatial] def render(mesh: MeshData) = {
		super.render(mesh)
		
		if ((range == null) || (range.length != mesh.length)) {
			range = 0 until mesh.length
		}
		
		glBegin(mesh.mode)
		this.mesh = mesh
		range.foreach(renderVertex)
		glEnd()
	}
	
	private val renderVertex = (index: Int) => {
		if (mesh.hasNormal) {
			val n = mesh.normal(index)
			if (n != null) {
				glNormal3d(n.x, n.y, n.z)
			}
		}
		if (mesh.hasColor) {
			val c = mesh.color(index)
			if (c != null) {
				glColor4d(c.red, c.green, c.blue, c.alpha)
			}
		}
		if (mesh.hasTexture) {
			val t = mesh.texture(index)
			if (t != null) {
				glTexCoord2d(t.x, t.y)
			}
		}
		val v = mesh.vertex(index)
		if (v != null) {
			glVertex3d(v.x, v.y, v.z)
		}
	}
}
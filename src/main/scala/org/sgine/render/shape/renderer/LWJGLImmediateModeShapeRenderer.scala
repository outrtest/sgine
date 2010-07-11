package org.sgine.render.shape.renderer

import org.lwjgl.opengl.GL11._

import org.sgine.render.shape.ShapeData

class LWJGLImmediateModeShapeRenderer extends LWJGLShapeRenderer {
	private var range: Range = _
	private var data: ShapeData = _
	
	protected[shape] def update(old: ShapeData, data: ShapeData) = {
		// Immediate mode, nothing to do
	}
	
	override protected[shape] def render(data: ShapeData) = {
		super.render(data)
		
		if ((range == null) || (range.length != data.length)) {
			range = 0 until data.length
		}

		this.data = data
		glBegin(data.mode)
		range.foreach(renderVertex)
		glEnd()
	}
	
	private val renderVertex = (index: Int) => {
		if (data.hasNormal) {
			val n = data.normal(index)
			if (n != null) {
				glNormal3d(n.x, n.y, n.z)
			}
		}
		if (data.hasColor) {
			val c = data.color(index)
			if (c != null) {
				glColor4d(c.red, c.green, c.blue, c.alpha)
			}
		}
		if (data.hasTexture) {
			val t = data.texture(index)
			if (t != null) {
				glTexCoord2d(t.x, t.y)
			}
		}
		val v = data.vertex(index)
		if (v != null) {
			glVertex3d(v.x, v.y, v.z)
		}
	}
}
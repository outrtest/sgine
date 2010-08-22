package org.sgine.render.shape.renderer.lwjgl

import org.lwjgl.opengl.GL11._

import org.sgine.render.shape._

class LWJGLImmediateModeShapeRenderer extends LWJGLShapeRenderer {
	private var range: Range = _
	private var shape: Shape = _
	
	protected[shape] def update(shape: Shape, vertexChanged: Boolean = false, colorChanged: Boolean = false, textureChanged: Boolean = false, normalChanged: Boolean = false) = {}
	
	override protected[shape] def renderInternal(shape: Shape) = {
		if ((range == null) || (range.length != shape.vertex.length)) {
			range = 0 until shape.vertex.length
		}

		this.shape = shape
		glBegin(shape.mode.value)
		range.foreach(renderVertex)
		glEnd()
	}
	
	private val renderVertex = (index: Int) => {
		if (shape.hasNormal) {
			val n = shape.normal(index)
			if (n != null) {
				glNormal3d(n.x, n.y, n.z)
			}
		}
		if (shape.hasColor) {
			val c = shape.color(index)
			if (c != null) {
				glColor4d(c.red, c.green, c.blue, c.alpha)
			}
		}
		if (shape.hasTexture) {
			val t = shape.texture(index)
			if (t != null) {
				glTexCoord2d(t.x, t.y)
			}
		}
		val v = shape.vertex(index)
		if (v != null) {
			glVertex3d(v.x, v.y, v.z)
		}
	}
}
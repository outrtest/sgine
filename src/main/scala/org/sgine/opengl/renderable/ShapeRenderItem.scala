package org.sgine.opengl.renderable

import org.sgine.math.Vector3
import org.lwjgl.opengl.GL11._;

class ShapeRenderItem extends RenderItem {
	def begin(renderable: Renderable, time: Double) = glBegin(GL_TRIANGLES)
	
	def vertex(renderable: Renderable, time: Double, index: Int, vertex: Vector3) = glVertex3d(vertex.x, vertex.y, vertex.z)
	
	def end(renderable: Renderable, time: Double) = glEnd()
}
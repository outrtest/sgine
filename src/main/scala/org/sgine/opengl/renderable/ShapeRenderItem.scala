package org.sgine.opengl.renderable

import org.sgine.math.VertexBuffer
import org.lwjgl.opengl.GL11._;

class ShapeRenderItem(buffer: VertexBuffer) extends RenderItem {
	private val v3d = glVertex3d _
	
	def begin(renderable: Renderable, time: Double) = glBegin(GL_TRIANGLES)
	
	def vertex(renderable: Renderable, time: Double, index: Int) = buffer(index, v3d)
	
	def end(renderable: Renderable, time: Double) = glEnd()
}
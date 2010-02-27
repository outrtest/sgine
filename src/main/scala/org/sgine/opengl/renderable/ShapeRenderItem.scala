package org.sgine.opengl.renderable

import org.sgine.math.VertexBuffer
import org.lwjgl.opengl.GL11._;

class ShapeRenderItem(buffer: VertexBuffer) extends RenderItem {
	private val v3f = glVertex3f _
	
	def begin(renderable: Renderable, time: Double) = glBegin(GL_TRIANGLES)
	
	def vertex(renderable: Renderable, time: Double, index: Int) = {
//		println("Index: " + index + " - " + buffer(0, index) + ", " + buffer(1, index) + ", " + buffer(2, index))
		buffer(index, v3f)
	}
	
	def end(renderable: Renderable, time: Double) = glEnd()
}
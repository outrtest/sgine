package org.sgine.opengl.renderable

import org.sgine.math.VertexBuffer
import org.lwjgl.opengl.GL11._

class TextureCoordinatesRenderItem(buffer: VertexBuffer) extends RenderItem {
	val tc2d = glTexCoord2d _
	
	def begin(renderable: Renderable, time: Double) = {}
	
	def vertex(renderable: Renderable, time: Double, index: Int) = buffer(index, tc2d)
	
	def end(renderable: Renderable, time: Double) = {}
}
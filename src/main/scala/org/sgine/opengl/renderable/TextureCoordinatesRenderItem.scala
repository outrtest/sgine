package org.sgine.opengl.renderable

import org.sgine.math.Vector2
import org.lwjgl.opengl.GL11._;

class TextureCoordinatesRenderItem(coordinates: Seq[Vector2]) extends RenderItem {
	def begin(renderable: Renderable, time: Double) = {}
	
	def vertex(renderable: Renderable, time: Double, index: Int) = glTexCoord2d(coordinates(index).x, coordinates(index).y)
	
	def end(renderable: Renderable, time: Double) = {}
}

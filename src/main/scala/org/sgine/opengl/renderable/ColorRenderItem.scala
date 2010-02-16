package org.sgine.opengl.renderable

import org.sgine.core.Color
import org.sgine.math.Vector3
import org.lwjgl.opengl.GL11._;

class ColorRenderItem(color: Color) extends RenderItem {
	def begin(renderable: Renderable, time: Double) = glColor4d(color.red * color.alpha, color.green * color.alpha, color.blue * color.alpha, color.alpha)
	
	def vertex(renderable: Renderable, time: Double, index: Int, vertex: Vector3) = {}
	
	def end(renderable: Renderable, time: Double) = {}
}
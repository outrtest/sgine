package org.sgine.opengl.renderable

import org.sgine.math.Vector3

trait RenderItem {
	def begin(renderable: Renderable, time: Double)
	
	def vertex(renderable: Renderable, time: Double, index: Int)
	
	def end(renderable: Renderable, time: Double)
}
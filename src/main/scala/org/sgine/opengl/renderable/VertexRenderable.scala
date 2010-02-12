package org.sgine.opengl.renderable

import org.sgine.math.Vector3

trait VertexRenderable extends Renderable {
	val points: List[Vector3]
	
	def renderVertex(time: Double, index: Int, vertex: Vector3): Unit = {
	}
}
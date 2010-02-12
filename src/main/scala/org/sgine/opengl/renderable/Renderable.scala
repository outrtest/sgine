package org.sgine.opengl.renderable

trait Renderable extends Function1[Double, Unit] {
	def apply(time: Double) = {
		renderBegin(time)
		
		this match {
			case v: VertexRenderable => {
				for (index <- 0 until v.points.length) {
					v.renderVertex(time, index, v.points(index))
				}
			}
		}
		
		renderEnd(time)
	}
	
	def renderBegin(time: Double): Unit = {
	}
	
	def renderEnd(time: Double): Unit = {
	}
}
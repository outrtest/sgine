package org.sgine.opengl.renderable

import org.sgine.math.Vector3

trait Renderable extends Function1[Double, Unit] {
	def renderItems: Seq[RenderItem]
	def vertices: Seq[Vector3]
	
	private var time: Double = 0.0
	private var indices: Range = 0 until 0
	
	def apply(time: Double) = {
		this.time = time
		val vertices = this.vertices
		if (indices.length != vertices.length) {
			indices = 0 until vertices.length
		}
		
		renderItems.foreach(renderBegin)
		indices.foreach(renderVertices)
		renderItems.foreach(renderEnd)
	}
	
	private val renderBegin = (item: RenderItem) => if (item != null) item.begin(this, time)
	
	private val renderVertices = (index: Int) => {
		for (item <- renderItems) {
			if (item != null) {
				item.vertex(this, time, index)
			}
		}
	}
	
	private val renderEnd = (item: RenderItem) => if (item != null) item.end(this, time)
}
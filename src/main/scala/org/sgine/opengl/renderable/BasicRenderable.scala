package org.sgine.opengl.renderable

import org.sgine.core.Color
import org.sgine.math.Matrix4
import org.sgine.math.Vector3

class BasicRenderable private (vertexCount: Int) extends MutableRenderable(4, vertexCount) {
	renderItems(0) = new MatrixRenderItem(Matrix4.Identity)
	renderItems(1) = new ColorRenderItem(Color.White)
	renderItems(3) = new ShapeRenderItem()
	
	def matrixItem = renderItems(0).asInstanceOf[MatrixRenderItem]
	def matrixItem_=(item: MatrixRenderItem) = renderItems(0) = item
	def colorItem = renderItems(1).asInstanceOf[ColorRenderItem]
	def colorItem_=(item: ColorRenderItem) = renderItems(1) = item
	def textureItem = renderItems(2)
	def textureItem_=(item: RenderItem) = renderItems(2) = item
	def shapeItem = renderItems(3).asInstanceOf[ShapeRenderItem]
	def shapeItem_=(item: ShapeRenderItem) = renderItems(3) = item
}

object BasicRenderable {
	def apply(vertexCount: Int) = new BasicRenderable(vertexCount)
	
	def apply(vertices: Seq[Vector3]) = {
		val r = new BasicRenderable(vertices.length)
		
		for (i <- 0 until vertices.length) {
			r.vertices(i) = vertices(i)
		}
		
		r
	}
}
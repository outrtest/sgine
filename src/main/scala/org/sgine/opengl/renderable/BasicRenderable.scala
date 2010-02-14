package org.sgine.opengl.renderable

import org.sgine.math.Matrix4

class BasicRenderable(vertexCount: Int) extends MutableRenderable(3, vertexCount) {
	renderItems(0) = new MatrixRenderItem(Matrix4.Identity)
	renderItems(2) = new ShapeRenderItem()
	
	def matrixItem = renderItems(0).asInstanceOf[MatrixRenderItem]
	def matrixItem_=(item: MatrixRenderItem) = renderItems(0) = item
	def textureItem = renderItems(1)
	def textureItem_=(item: RenderItem) = renderItems(1) = item
	def shapeItem = renderItems(2).asInstanceOf[ShapeRenderItem]
	def shapeItem_=(item: ShapeRenderItem) = renderItems(2) = item
}
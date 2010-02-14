package org.sgine.opengl.renderable

import org.sgine.math.Vector3
import scala.collection.mutable.WrappedArray

class MutableRenderable (itemCount: Int, vertexCount: Int) extends Renderable {
	val renderItems: WrappedArray[RenderItem] = WrappedArray.make(new Array[RenderItem](itemCount))
	val vertices: WrappedArray[Vector3] = WrappedArray.make(new Array[Vector3](vertexCount))
}
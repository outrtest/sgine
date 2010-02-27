package org.sgine.opengl.renderable

import org.sgine.math.Vector3
import scala.collection.mutable.WrappedArray

class MutableRenderable (itemCount: Int, val vertexCount: Int) extends Renderable {
	val renderItems: WrappedArray[RenderItem] = WrappedArray.make(new Array[RenderItem](itemCount))
}
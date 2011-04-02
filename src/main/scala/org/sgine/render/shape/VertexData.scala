package org.sgine.render.shape

import simplex3d.math.doublem.renamed._

case class VertexData(data: Seq[Vec3]) {
	def length = data.length
	
	def apply(index: Int) = data(index)
	
	override def toString() = "VertexData(" + data + ")"
}
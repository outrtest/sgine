package org.sgine.render.shape

import simplex3d.math.doublem.renamed._

case class NormalData(data: Seq[Vec3]) {
	def apply(index: Int) = data(index)
}
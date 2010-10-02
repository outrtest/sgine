package org.sgine.render.shape

import simplex3d.math.doublem.renamed._

case class TextureData(data: Seq[Vec2]) {
	def apply(index: Int) = data(index)
}
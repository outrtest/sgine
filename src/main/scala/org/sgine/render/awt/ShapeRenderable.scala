package org.sgine.render.awt

trait ShapeRenderable extends Function0[Unit]

object ShapeRenderable {
	def apply(shape: java.awt.Shape, cacheAsTexture: Boolean = true) = cacheAsTexture match {
		case true => new ShapeRenderableTexture(shape)
		case false => new ShapeRenderableGL(shape)
	}
}
package org.sgine.render

trait RenderUpdatable {
	protected def update(renderer: Renderer)
}

object RenderUpdatable {
	def update(renderer: Renderer, updatable: RenderUpdatable) = {
		updatable.update(renderer)
	}
}
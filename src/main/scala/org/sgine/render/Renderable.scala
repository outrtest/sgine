package org.sgine.render

import org.sgine.scene.Node

trait Renderable extends Node {
	def render(renderer: Renderer)
}
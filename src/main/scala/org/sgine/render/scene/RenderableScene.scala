package org.sgine.render.scene

import org.sgine.render.FPS
import org.sgine.render.Renderable

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer
import org.sgine.scene.view.NodeView

class RenderableScene private(val scene: NodeContainer, val showFPS: Boolean) extends Renderable {
	val view = NodeView(scene, RenderableQuery, false)
	val fps = FPS()
	
	def render() = {
		view.foreach(renderItem)
		if (showFPS) fps()
	}
	
	private val renderItem = (n: Node) => n.asInstanceOf[Renderable].render()
}

object RenderableScene {
	def apply(scene: NodeContainer, showFPS: Boolean = true) = new RenderableScene(scene, showFPS)
}
package org.sgine.render.scene

import org.sgine.render.Renderable

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer
import org.sgine.scene.view.NodeView

class RenderableScene private(val scene: NodeContainer) extends Renderable {
	val view = NodeView(scene, RenderableQuery, false)
	
	def render() = view.foreach(renderItem)
	
	private val renderItem = (n: Node) => n.asInstanceOf[Renderable].render()
}

object RenderableScene {
	def apply(scene: NodeContainer) = new RenderableScene(scene)
}
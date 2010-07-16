package org.sgine.render.scene

import org.sgine.render.Renderable

import org.sgine.scene.Node
import org.sgine.scene.query.NodeQuery

object RenderableQuery extends NodeQuery {
	def matches(node: Node) = node.isInstanceOf[Renderable]
}
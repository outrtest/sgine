package org.sgine.render.scene

import org.sgine.render.RenderUpdatable

import org.sgine.scene.Node
import org.sgine.scene.query.NodeQuery

object RenderUpdatableQuery extends NodeQuery {
	def matches(node: Node) = node.isInstanceOf[RenderUpdatable]
}
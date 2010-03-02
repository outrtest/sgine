package org.sgine.visual.scene

import org.sgine.scene.Node
import org.sgine.scene.query.NodeQuery
import org.sgine.visual.Shape

object ShapeQuery extends NodeQuery {
	def matches(node: Node) = node.isInstanceOf[Shape]
}
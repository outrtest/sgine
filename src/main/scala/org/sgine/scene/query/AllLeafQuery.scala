package org.sgine.scene.query

import org.sgine.scene._

object AllLeafQuery extends NodeQuery {
	def matches(node: Node) = !node.isInstanceOf[NodeContainer]
}

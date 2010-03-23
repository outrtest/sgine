package org.sgine.scene.query

import org.sgine.Updatable
import org.sgine.scene.Node

object UpdatablesQuery extends NodeQuery {
	def matches(node: Node) = node.isInstanceOf[Updatable]
}

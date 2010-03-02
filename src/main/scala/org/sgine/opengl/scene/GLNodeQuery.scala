package org.sgine.opengl.scene

import org.sgine.scene._
import org.sgine.scene.query._

object GLNodeQuery extends NodeQuery {
	def matches(node: Node) = node.isInstanceOf[GLNode]
}
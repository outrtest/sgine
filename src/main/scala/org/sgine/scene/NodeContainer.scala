package org.sgine.scene


import org.sgine.scene.query._
import org.sgine.scene.view.NodeView

/**
 * Something that contains Nodes and can fetch the ones mathcing a query.
 */
trait NodeContainer extends Node with Iterable[Node] {
	def indexOf(n: Node): Int = {
		var index = 0
		for (c <- this) {
			if (c == n) {
				return index
			}
			
			index += 1
		}
		
		return -1
	}
}
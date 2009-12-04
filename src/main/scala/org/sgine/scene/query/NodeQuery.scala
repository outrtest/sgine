package org.sgine.scene.query

import org.sgine.scene._

/**
 * A query for retrieving some subset of Nodes form a NodeContainer.
 */
trait NodeQuery {
	/**
	 * Return true if the query should return the specified node.
	 */
	def matches(node: Node): Boolean
}

object NodeQuery {
	def query(q:NodeQuery, container: NodeContainer): List[Node] = {
		query(q, container, Nil)
	}
	
	private def query(q:NodeQuery, container: NodeContainer, list:List[Node]): List[Node] = {
		// TODO: is List the best way to represent this?
		// TODO: maybe a long-term modifiable collection?
		var l = list
		for (n <- container) {
			n match {
				case nc: NodeContainer => l = query(q, container, l)
				case _ => {
					if (q.matches(n)) {
						n :: l
					}
				}
			}
		}
		
		l
	}
}
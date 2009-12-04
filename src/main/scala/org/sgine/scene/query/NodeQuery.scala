package org.sgine.scene.query

import org.sgine.scene._

import java.util._

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
	def queryInto(q: NodeQuery, container: NodeContainer, collection:Collection[Node]): Unit = {
		for (n <- container) {
			n match {
				case nc: NodeContainer => queryInto(q, container, collection)
				case _ => {
					if (q.matches(n)) {
						collection.add(n)
					}
				}
			}
		}
	}
}
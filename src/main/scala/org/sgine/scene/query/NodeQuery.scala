package org.sgine.scene.query

import org.sgine.scene._

import java.util._

/**
 * A query for retrieving some subset of Nodes form a NodeContainer.
 */
trait NodeQuery extends (Node => Boolean) {
	def apply(node: Node): Boolean = matches(node)
	
	/**
	 * Return true if the query should return the specified node.
	 */
	def matches(node: Node): Boolean
}


object NodeQuery {

  /**
   * Runs the specified query on the specified node, invoking the callback function f with all matches.
   */
	def query(q: Node => Boolean, node: Node, f: Node => Unit): Unit = {
		if (q(node)) {
			f(node)
		}
		node match {
			case container: NodeContainer => container.children.foreach(query(q, _, f))
			case _ =>
		}
	}
}
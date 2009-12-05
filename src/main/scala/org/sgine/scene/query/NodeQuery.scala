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

  /**
   * Runs the specified query on the specified node, invoking the callback function f with all matches.
   */
	def query(q: NodeQuery, node: Node, f: Node => Unit): Unit = node match {
		case container: NodeContainer => container foreach { query(q, _, f) }
		case _ => if (q.matches(node)) f(node)
	}
}
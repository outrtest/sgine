package org.sgine.scene.query

import org.sgine.scene.Node

/**
 * A query for retrieving some subset of Nodes form a NodeContainer.
 */
trait NodeQuery {

  /**
   * Return true if the query should return the specified node.
   */
  def matches(node: Node): Boolean
}

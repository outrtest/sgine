package com.sgine.scene.query

/**
 * A query that retrieves all nodes.
 */
object AllQuery extends NodeQuery {
  def matches(node: Node) = true
}

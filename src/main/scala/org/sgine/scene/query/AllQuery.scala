package org.sgine.scene.query

import org.sgine.scene.Node

/**
 * A query that retrieves all nodes.
 */
object AllQuery extends NodeQuery {

  def matches(node: Node) = true
}

package com.sgine.scene

import collection.Iterator
import java.util.concurrent._;

/**
 * A Node that can contain child nodes.
 */
trait BranchNode extends Node {

  /**
   * Returns the child nodes of this BranchNode.
   *
   * Does not necessarily create generated childNodes.
   */
  def children : Iterator[Node]

  /**
   * Returns the child nodes of this BranchNode that match the specified query.
   */
  // TODO:
  // def children( query : NodeQuery ) : Iterator[Node]

  /**
   * Creates a view of this BranchNode, showing any children that match the query.
   * The view is updated automatically as child nodes appear or dissappear from the query results.
   */
  // TODO:
  // def createView( query : NodeQuery ) : NodeView

}

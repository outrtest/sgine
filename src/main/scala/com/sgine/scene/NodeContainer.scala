package com.sgine.scene


import query._

/**
 * Something that contains Nodes and can fetch the ones mathcing a query.
 */
trait NodeContainer {

  /**
   * Return an iterator over all the Node:s in the container.
   */
  def getNodes(): Iterator[Node] = getNodes(AllQuery)

  /**
   * Return an iterator over all the Node:s matching the specified condition in the container.
   */
  def getNodes(condition : Node => Boolean): Iterator[Node] = getNodes( ConditionNodeQuery( condition ) )

  /**
   * Return an iterator over all the Node:s matching the specified query in the container.
   */
  def getNodes(query : NodeQuery): Iterator[Node]

}

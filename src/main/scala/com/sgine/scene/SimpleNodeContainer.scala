package com.sgine.scene

import query.NodeQuery
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.JavaConversions._

/**
 * NodeContainer backed up by a simple collection.
 */
class SimpleNodeContainer extends NodeContainer {

  private val nodes = new ConcurrentLinkedQueue[Node]()

  def getNodes(query: NodeQuery) = nodes filter( query.matches(_) )

  def removeNode(node: Node) {
    nodes.remove( node )
    node.setParent(null)
  }

  def addNode(node: Node) {
    nodes.add(node)
    node.setParent(this)
  }
}

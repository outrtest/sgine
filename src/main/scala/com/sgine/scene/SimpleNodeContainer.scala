package com.sgine.scene

import query.NodeQuery
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.JavaConversions._

/**
 * NodeContainer backed up by a simple collection.
 */
class SimpleNodeContainer extends EditableNodeContainer {

  private val nodes = new ConcurrentLinkedQueue[Node]()

  def getNodes(query: NodeQuery) = nodes filter( query.matches(_) )

  def addNode(node: Node) {
    require(node != null,"Can not add node, the node should not be null.")
    require(!nodes.contains(node), "Can not add node, node already in container.  " +
                                   "The node '"+node+"' is already in the container '"+this+"'.")

    nodes.add(node)
    node.setParent(this)
  }

  def removeNode(node: Node): Boolean = {
    val removed = nodes.remove( node )
    if (removed) node.setParent(null)
    removed
  }
}

package org.sgine.scene

import query.NodeQuery
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.JavaConversions._
import view.NodeView
import java.lang.UnsupportedOperationException

/**
 * NodeContainer backed up by a thread-safe collection.
 */
class GeneralNodeContainer extends MutableNodeContainer {

  private val nodes = new ConcurrentLinkedQueue[Node]()

  def iterator = nodes.iterator

  def +=(node: Node) {
    require(node != null,"Can not add node, the node should not be null.")
    require(!nodes.contains(node), "Can not add node, node already in container.  " +
                                   "The node '"+node+"' is already in the container '"+this+"'.")

    nodes.add(node)
    node.parent = this
  }

  def -=(node: Node): Boolean = {
    val removed = nodes.remove( node )
    if (removed) node.parent = null
    removed
  }

  def createView(query: NodeQuery): NodeView = {
    // TODO: Implement
    // We'll need to notify the view about added and removed nodes in this container.
    throw new UnsupportedOperationException("Not implemented yet")
  }
}

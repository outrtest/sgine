package org.sgine.scene

import query.NodeQuery
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.JavaConversions._
import view.NodeView
import java.lang.UnsupportedOperationException

import org.sgine.event._
import org.sgine.scene.event._

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
    require(node.parent == null,"Can not add node, the node is already in another container '"+node.parent+"'.")

    nodes.add(node)
    node.parent = this
    
    Event.enqueue(new NodeContainerEvent(this, node, SceneEventType.ChildAdded))
  }

  def -=(node: Node): Boolean = {
    val removed = nodes.remove( node )
    
    if (removed) {
    	node.parent = null
    	
    	Event.enqueue(new NodeContainerEvent(this, node, SceneEventType.ChildRemoved))
    }

    removed
  }
}

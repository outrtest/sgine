package org.sgine.scene

import query.NodeQuery
import java.util.concurrent.ConcurrentLinkedQueue
import view.NodeView
import java.lang.UnsupportedOperationException

import org.sgine.event._
import org.sgine.scene.event._

/**
 * NodeContainer backed up by a thread-safe collection.
 */
class GeneralNodeContainer extends MutableNodeContainer {
	val children = new scala.collection.mutable.ArrayBuffer[Node]()

  def +=(node: Node) {
    require(node != null,"Can not add node, the node should not be null.")
    require(!children.contains(node), "Can not add node, node already in container.  " +
                                   "The node '"+node+"' is already in the container '"+this+"'.")
    require(node.parent == null,"Can not add node, the node is already in another container '"+node.parent+"'.")

    synchronized {
    	children += node
    }
    
    node.parent = this
    
    Event.enqueue(NodeContainerEvent(this, node, SceneEventType.ChildAdded))
    Event.enqueue(SceneEvent(node, SceneEventType.ParentChanged))
  }

  def -=(node: Node): Boolean = {
	  synchronized {
	 	  if (children.contains(node)) {
	 	 	  children -= node
	 	 	  node.parent = null
	 	 	  
	 	 	  Event.enqueue(NodeContainerEvent(this, node, SceneEventType.ChildRemoved))
	 	 	  Event.enqueue(SceneEvent(node, SceneEventType.ParentChanged))
	 	 	  
	 	 	  true
	 	  } else {
	 	 	  false
	 	  }
	 }
  }
}

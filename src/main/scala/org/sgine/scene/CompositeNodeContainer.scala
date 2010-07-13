package org.sgine.scene

import org.sgine.event.Event

import org.sgine.scene.event.NodeContainerEvent
import org.sgine.scene.event.SceneEvent
import org.sgine.scene.event.SceneEventType

trait CompositeNodeContainer extends NodeContainer {
	private var nodes = new scala.collection.mutable.ArrayBuffer[Node]()

  def iterator = if (nodes != null) nodes.iterator else null

  protected def +=(node: Node) {
	  nodes += node
    
    node.parent = this
    
    Event.enqueue(NodeContainerEvent(this, node, SceneEventType.ChildAdded))
    Event.enqueue(SceneEvent(node, SceneEventType.ParentChanged))
  }

  protected def -=(node: Node): Boolean = {
 	  if (nodes.contains(node)) {
 	 	  nodes -= node
 	 	  node.parent = null
 	 	  
 	 	  Event.enqueue(NodeContainerEvent(this, node, SceneEventType.ChildRemoved))
 	 	  Event.enqueue(SceneEvent(node, SceneEventType.ParentChanged))
 	 	  
 	 	  true
 	  } else {
 	 	  false
 	  }
  }
}
package org.sgine.scene

import org.sgine.event.Event

import org.sgine.scene.event.NodeContainerEvent
import org.sgine.scene.event.SceneEvent
import org.sgine.scene.event.SceneEventType

trait CompositeNodeContainer extends NodeContainer {
	val children = new scala.collection.mutable.ArrayBuffer[Node]()
	
	protected def +=(node: Node) {
		synchronized {
			children += node
		}
    
		node.parent = this
    
		Event.enqueue(NodeContainerEvent(this, node, SceneEventType.ChildAdded))
		Event.enqueue(SceneEvent(node, SceneEventType.ParentChanged))
	}

	protected def -=(node: Node): Boolean = {
		if (children.contains(node)) {
			synchronized {
				children -= node
			}
 	 	  	node.parent = null
 	 	  
 	 	  	Event.enqueue(NodeContainerEvent(this, node, SceneEventType.ChildRemoved))
 	 	  	Event.enqueue(SceneEvent(node, SceneEventType.ParentChanged))
 	 	  
 	 	  	true
 	 	} else {
 	 		false
 	 	}
	}
}
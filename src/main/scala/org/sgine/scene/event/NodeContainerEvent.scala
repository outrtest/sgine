package org.sgine.scene.event

import org.sgine.event.Event

import org.sgine.scene._

class NodeContainerEvent(val parent: NodeContainer, val child: Node, eventType: SceneEventType.Value) extends SceneEvent(parent, eventType) {
	override def retarget(target: org.sgine.event.Listenable): Event = new NodeContainerEvent(target.asInstanceOf[NodeContainer], child, eventType)
}
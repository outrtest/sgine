package org.sgine.scene.event

import org.sgine.event.Event

import org.sgine.scene._

class NodeContainerEvent private(val parent: NodeContainer, val child: Node, eventType: SceneEventType.Value) extends SceneEvent(parent, eventType) {
	override def retarget(target: org.sgine.event.Listenable): Event = new NodeContainerEvent(target.asInstanceOf[NodeContainer], child, eventType)
}

object NodeContainerEvent {
	def apply(parent: NodeContainer, child: Node, eventType: SceneEventType.Value) = new NodeContainerEvent(parent, child, eventType)
}
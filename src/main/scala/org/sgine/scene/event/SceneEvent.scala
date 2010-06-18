package org.sgine.scene.event

import org.sgine.event._
import org.sgine.scene._

class SceneEvent private[event](node: Node, val eventType: SceneEventType.Value) extends Event(node) {
	def retarget(target: org.sgine.event.Listenable): Event = new SceneEvent(target.asInstanceOf[Node], eventType)
}

object SceneEvent {
	def apply(node: Node, eventType: SceneEventType.Value) = new SceneEvent(node, eventType)
}
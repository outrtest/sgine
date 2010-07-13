package org.sgine.scene.view.event

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.view._

case class NodeRemovedEvent(view: NodeView, node: Node) extends Event(view) {
	def retarget(target: Listenable): Event = new NodeRemovedEvent(target.asInstanceOf[NodeView], node)
}
package org.sgine.scene.view.event

import org.sgine.event._

import org.sgine.scene._
import org.sgine.scene.view._

case class NodeAddedEvent(view: NodeView, node: Node) extends Event(view)
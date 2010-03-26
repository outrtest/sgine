package org.sgine.scene.event

import org.sgine.event._
import org.sgine.scene._

class SceneEvent(node: Node, val eventType: SceneEventType.Value) extends Event(node)
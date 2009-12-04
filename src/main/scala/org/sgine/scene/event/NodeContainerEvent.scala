package org.sgine.scene.event

import org.sgine.scene._

class NodeContainerEvent(val parent: NodeContainer, val child: Node, eventType: SceneEventType.Value) extends SceneEvent(parent, eventType)
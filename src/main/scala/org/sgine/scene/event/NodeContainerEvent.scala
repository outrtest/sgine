package org.sgine.scene.event

import org.sgine.scene._

class NodeContainerEvent(parent: NodeContainer, child: Node, eventType: SceneEventType.Value) extends SceneEvent(parent, eventType)
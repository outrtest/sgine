package org.sgine.ui

import org.sgine.event.Event

import org.sgine.scene.CompositeNodeContainer
import org.sgine.scene.MatrixNode

// TODO: should this remain a Component or should it just be a NodeContainer?
trait CompositeComponent extends Component with CompositeNodeContainer with MatrixNode {
	protected def drawComponent() = {
	}
}
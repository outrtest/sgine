package org.sgine.ui

import org.sgine.event.Event

import org.sgine.scene.CompositeNodeContainer
import org.sgine.scene.MatrixNode

trait CompositeComponent extends Component with CompositeNodeContainer with MatrixNode {
	protected def drawComponent() = {
	}
}
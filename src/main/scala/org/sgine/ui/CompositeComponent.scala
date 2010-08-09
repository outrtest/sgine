package org.sgine.ui

import org.sgine.event.Event

import org.sgine.scene.CompositeNodeContainer

trait CompositeComponent extends Component with CompositeNodeContainer {
	protected[ui] def drawComponent() = {
	}
}
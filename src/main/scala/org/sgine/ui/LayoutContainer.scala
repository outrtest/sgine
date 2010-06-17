package org.sgine.ui

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.scene.GeneralNodeContainer
import org.sgine.ui.layout.LayoutNode

import org.sgine.ui.ext.AdvancedComponent

class LayoutContainer extends GeneralNodeContainer with AdvancedComponent with LayoutNode with BoundingObject {
	protected val _bounding = new BoundingBox

	protected def drawComponent() = {
	}
}
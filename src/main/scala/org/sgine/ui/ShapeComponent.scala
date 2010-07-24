package org.sgine.ui

import org.sgine.bounding.BoundingObject

import org.sgine.property.AdvancedProperty

import org.sgine.render.shape.Shape

import org.sgine.ui.ext.AdvancedComponent

trait ShapeComponent extends Component {
	protected val shape = Shape()
	
	protected[ui] def drawComponent() = shape()
}

object ShapeComponent {
	def apply() = new ShapeComponentImpl()
}

class ShapeComponentImpl extends AdvancedComponent with ShapeComponent		// TODO: make BoundingObject
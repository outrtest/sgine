package org.sgine.ui

import org.sgine.bounding.BoundingObject

import org.sgine.property.AdvancedProperty

import org.sgine.render.shape.Shape

import org.sgine.ui.ext.AdvancedComponent

trait ShapeComponent extends Component {
	val shape = new AdvancedProperty[Shape](null, this)
	
	protected def drawComponent() = {
		shape() match {
			case null =>
			case s => s()
		}
	}
}

object ShapeComponent {
	def apply() = new ShapeComponentImpl()
}

class ShapeComponentImpl extends AdvancedComponent with ShapeComponent		// TODO: make BoundingObject
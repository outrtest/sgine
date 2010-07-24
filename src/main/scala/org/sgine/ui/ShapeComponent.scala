package org.sgine.ui

import org.sgine.bounding.BoundingObject

import org.sgine.property.AdvancedProperty

import org.sgine.render.Renderer
import org.sgine.render.Texture
import org.sgine.render.shape.Shape

import org.sgine.ui.ext.AdvancedComponent

trait ShapeComponent extends Component {
	protected var texture: Texture = _
	protected val shape = Shape()
	
	override def update(renderer: Renderer) = {
		super.update(renderer)
		
		shape.update()
	}
	
	protected[ui] def drawComponent() = {
		if (texture != null) {
			texture.bind()
		} else {
			Texture.unbind()
		}
		shape.render()
	}
}

object ShapeComponent {
	def apply() = new ShapeComponentImpl()
}

class ShapeComponentImpl extends AdvancedComponent with ShapeComponent		// TODO: make BoundingObject
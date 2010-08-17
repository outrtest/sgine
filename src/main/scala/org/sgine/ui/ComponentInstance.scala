package org.sgine.ui

import org.sgine.bounding.BoundingObject

import org.sgine.path.OPath

import org.sgine.property.AdvancedProperty

/**
 * ComponentInstance refers to an existing Component to render
 * on the screen. This allows a single Component to be "instanced"
 * on the screen multiple times in multiple locations.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentInstance private() extends Component {
	val instance = new AdvancedProperty[Component](null, this)
	
	bounding.bindPath(OPath(this, "instance.bounding"))
	
	protected[ui] def drawComponent() = {
		instance() match {
			case null =>
			case c => c.drawComponent()
		}
	}
}

object ComponentInstance {
	def apply(instance: Component = null) = {
		val c = new ComponentInstance()
		if (instance != null) {
			c.instance := instance
		}
		c
	}
}
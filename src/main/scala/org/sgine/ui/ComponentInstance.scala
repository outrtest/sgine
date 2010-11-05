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
	
	protected def this(instance: Component) = {
		this()
		
		this.instance := instance
	}
	
	bounding.bindPath(OPath(this, "instance().bounding"))
	size.measured.width.bindPath(OPath(this, "instance().size.measured.width"))
	size.measured.height.bindPath(OPath(this, "instance().size.measured.height"))
	size.measured.depth.bindPath(OPath(this, "instance().size.measured.depth"))
	
	def drawComponent() = {
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
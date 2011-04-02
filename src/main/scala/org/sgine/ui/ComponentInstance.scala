package org.sgine.ui

import org.sgine.bounding.BoundingObject

import org.sgine.core._

import org.sgine.path.OPath

import org.sgine.property.AdvancedProperty

/**
 * ComponentInstance refers to an existing Component to render
 * on the screen. This allows a single Component to be "instanced"
 * on the screen multiple times in multiple locations.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait ComponentInstance extends Component {
	val instance = new AdvancedProperty[Component](null, this)
	val instanceColor = new AdvancedProperty[Color](Color.White, this)
	
	bounding.bindPath(OPath(this, "instance().bounding"))
	size.measured.width.bindPath(OPath(this, "instance().size.measured.width"))
	size.measured.height.bindPath(OPath(this, "instance().size.measured.height"))
	size.measured.depth.bindPath(OPath(this, "instance().size.measured.depth"))
	
	def drawComponent() = {
		multColor(instanceColor())
		instance() match {
			case null =>
			case c => c.drawComponent()
		}
	}
}

class ComponentInstanceImpl extends ComponentInstance {
	def this(original: Component) = {
		this()
		
		instance := original
	}
}

object ComponentInstance {
	def apply(instance: Component = null) = {
		val c = new ComponentInstanceImpl
		if (instance != null) {
			c.instance := instance
		}
		c
	}
}
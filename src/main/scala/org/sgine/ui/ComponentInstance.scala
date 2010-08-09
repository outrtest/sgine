package org.sgine.ui

import org.sgine.bounding.BoundingObject

import org.sgine.property.AdvancedProperty

import org.sgine.ui.ext.AdvancedComponent

/**
 * ComponentInstance refers to an existing Component to render
 * on the screen. This allows a single Component to be "instanced"
 * on the screen multiple times in multiple locations.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ComponentInstance private() extends AdvancedComponent with BoundingObject {
	val instance = new AdvancedProperty[Component](null, this)
	
	protected def _bounding = instance() match {
		case bo: BoundingObject => bo.bounding()
		case _ => null
	}
	
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
package org.sgine.ui.ext

import org.sgine.bounding.mutable.{BoundingBox => MutableBoundingBox}

import org.sgine.event._

import org.sgine.property.NumericProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.NumericPropertyChangeEvent

import org.sgine.ui.Component

class Dimension(override val parent: Component) extends PropertyContainer {
	val width = new NumericProperty(0.0, this)
	val height = new NumericProperty(0.0, this)
	val depth = new NumericProperty(0.0, this)
	
	private val handler = EventHandler(propertyChanged, ProcessingMode.Blocking)
	width.listeners += handler
	height.listeners += handler
	depth.listeners += handler
	
	def set(width: Double, height: Double) = {
		this.width := width
		this.height := height
	}
	
	def set(width: Double, height: Double, depth: Double) = {
		this.width := width
		this.height := height
		this.depth := depth
	}
	
	private def propertyChanged(evt: NumericPropertyChangeEvent) = {
		parent.bounding() match {
			case mbb: MutableBoundingBox => {
				mbb.width = width()
				mbb.height = height()
				mbb.depth = depth()
				parent.bounding.changed(mbb, mbb)
			}
			case _ => parent.bounding := MutableBoundingBox(width(), height(), depth())
		}
	}
}
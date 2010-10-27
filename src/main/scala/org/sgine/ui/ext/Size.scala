package org.sgine.ui.ext

import org.sgine.bounding.mutable.{BoundingBox => MutableBoundingBox}

import org.sgine.core.ProcessingMode

import org.sgine.event._

import org.sgine.property.NumericProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.style.Stylized

import org.sgine.ui.Component

class Size(override val parent: Component) extends PropertyContainer with Stylized {
	val width = new SizeNumericProperty(0.0, this)
	val height = new SizeNumericProperty(0.0, this)
	val depth = new SizeNumericProperty(0.0, this)
	
	private val handler = EventHandler(propertyChanged, ProcessingMode.Blocking)
	width.listeners += handler
	height.listeners += handler
	depth.listeners += handler
	
	def apply(width: Double, height: Double) = {
		this.width := width
		this.height := height
	}
	
	def apply(width: Double, height: Double, depth: Double) = {
		this.width := width
		this.height := height
		this.depth := depth
	}
	
	private def propertyChanged(evt: PropertyChangeEvent[_]) = {
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
	
	override def toString() = "Size(" + width() + ", " + height() + ", " + depth() + ")"
}
package org.sgine.ui.ext

import org.sgine.bounding.mutable.{BoundingBox => MutableBoundingBox}

import org.sgine.core.ProcessingMode
import org.sgine.core.SizeMode

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
	val measured = new MeasuredSize(this)
	val actual = new ActualSize(this)
	
	Listenable.listenTo(EventHandler(widthChanged, ProcessingMode.Blocking), width, width.mode)
	Listenable.listenTo(EventHandler(heightChanged, ProcessingMode.Blocking), height, height.mode)
	Listenable.listenTo(EventHandler(depthChanged, ProcessingMode.Blocking), depth, depth.mode)
	
	def apply(width: Double, height: Double) = {
		this.width := width
		this.height := height
	}
	
	def apply(width: Double, height: Double, depth: Double) = {
		this.width := width
		this.height := height
		this.depth := depth
	}
	
	private def widthChanged(evt: PropertyChangeEvent[_]) = {
		if (width.mode() == SizeMode.Explicit) {
			actual.width := width()
		} else {
			actual.width := measured.width()
		}
	}
	
	private def heightChanged(evt: PropertyChangeEvent[_]) = {
		if (height.mode() == SizeMode.Explicit) {
			actual.height := height()
		} else {
			actual.height := measured.height()
		}
	}
	
	private def depthChanged(evt: PropertyChangeEvent[_]) = {
		if (depth.mode() == SizeMode.Explicit) {
			actual.depth := depth()
		} else {
			actual.depth := measured.depth()
		}
	}
	
	override def toString() = "Size(" + width() + ", " + height() + ", " + depth() + ")"
}

class MeasuredSize(override val parent: Size) extends PropertyContainer with Stylized {
	val width = new NumericProperty(0.0, this)
	val height = new NumericProperty(0.0, this)
	val depth = new NumericProperty(0.0, this)
	
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
		if (parent.width.mode() == SizeMode.Auto) {
			parent.actual.width := width()
		}
		if (parent.height.mode() == SizeMode.Auto) {
			parent.actual.height := height()
		}
		if (parent.depth.mode() == SizeMode.Auto) {
			parent.actual.depth := depth()
		}
	}
	
	override def toString() = "MeasuredSize(" + width() + ", " + height() + ", " + depth() + ")"
}

class ActualSize(override val parent: Size) extends PropertyContainer with Stylized {
	val width = new NumericProperty(0.0, this)
	val height = new NumericProperty(0.0, this)
	val depth = new NumericProperty(0.0, this)
	
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
		val component = parent.parent
		component.bounding() match {
			case mbb: MutableBoundingBox => {
				mbb.width = width()
				mbb.height = height()
				mbb.depth = depth()
				component.bounding.changedLocal()
			}
			case _ => component.bounding := MutableBoundingBox(width(), height(), depth())
		}
	}
	
	override def toString() = "ActualSize(" + width() + ", " + height() + ", " + depth() + ")"
}
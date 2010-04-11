package org.sgine.ui

import org.sgine.property.AdvancedProperty

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.RotationComponent

trait Cube[T <: Component with RotationComponent]extends CompositeComponent with AdvancedComponent {
	val front = new AdvancedProperty[T](createComponent(), this)
	val back = new AdvancedProperty[T](createComponent(), this)
	val top = new AdvancedProperty[T](createComponent(), this)
	val bottom = new AdvancedProperty[T](createComponent(), this)
	val left = new AdvancedProperty[T](createComponent(), this)
	val right = new AdvancedProperty[T](createComponent(), this)
	
	back().rotation.y := Math.Pi
	top().rotation.x := Math.Pi / -2.0
	bottom().rotation.x := Math.Pi / 2.0
	left().rotation.y := Math.Pi / -2.0
	right().rotation.y := Math.Pi / 2.0
	
	val children = front() :: back() :: top() :: bottom() :: left() :: right() :: Nil
	
	protected def createComponent(): T
}
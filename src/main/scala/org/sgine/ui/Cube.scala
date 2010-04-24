package org.sgine.ui

import org.sgine.property.ImmutableProperty

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.RotationComponent

trait Cube[T <: Component with RotationComponent] extends CompositeComponent with AdvancedComponent {
	val front = new ImmutableProperty[T](createComponent())
	val back = new ImmutableProperty[T](createComponent())
	val top = new ImmutableProperty[T](createComponent())
	val bottom = new ImmutableProperty[T](createComponent())
	val left = new ImmutableProperty[T](createComponent())
	val right = new ImmutableProperty[T](createComponent())
	
	back().rotation.y := Math.Pi
	top().rotation.x := Math.Pi / -2.0
	bottom().rotation.x := Math.Pi / 2.0
	left().rotation.y := Math.Pi / -2.0
	right().rotation.y := Math.Pi / 2.0
	
	val children = front() :: back() :: top() :: bottom() :: left() :: right() :: Nil
	
	protected def createComponent(): T
}
package org.sgine.ui

import org.sgine.property.ImmutableProperty

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.RotationComponent

import scala.math._

trait Cube[T <: Component with RotationComponent] extends CompositeComponent with AdvancedComponent {
	val front = new ImmutableProperty[T](createComponent())
	val back = new ImmutableProperty[T](createComponent())
	val top = new ImmutableProperty[T](createComponent())
	val bottom = new ImmutableProperty[T](createComponent())
	val left = new ImmutableProperty[T](createComponent())
	val right = new ImmutableProperty[T](createComponent())
	
	back().rotation.y := Pi
	top().rotation.x := Pi / -2.0
	bottom().rotation.x := Pi / 2.0
	left().rotation.y := Pi / -2.0
	right().rotation.y := Pi / 2.0
	
	this += front()
	this += back()
	this += top()
	this += bottom()
	this += left()
	this += right()
	
	protected def createComponent(): T
}
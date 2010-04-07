package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.property.ImmutableProperty

import org.sgine.ui.ext.LocationComponent
import org.sgine.ui.ext.RotationComponent
import org.sgine.ui.ext.ScaleComponent

class Cube extends CompositeComponent with LocationComponent with RotationComponent with ScaleComponent {
	val front = new ImmutableProperty[Image](new Image())
	val back = new ImmutableProperty[Image](new Image())
	val top = new ImmutableProperty[Image](new Image())
	val bottom = new ImmutableProperty[Image](new Image())
	val left = new ImmutableProperty[Image](new Image())
	val right = new ImmutableProperty[Image](new Image())
	
	back().rotation.y := Math.Pi
	top().rotation.x := Math.Pi / 2.0
	bottom().rotation.x := Math.Pi / -2.0
	left().rotation.y := Math.Pi / -2.0
	right().rotation.y := Math.Pi / 2.0
	
	val children = front() :: back() :: top() :: bottom() :: left() :: right() :: Nil
	
	def apply(resource: Resource, width: Double, height: Double) = {
		front().source := resource
		back().source := resource
		top().source := resource
		bottom().source := resource
		left().source := resource
		right().source := resource
		
		front().location.z := width / 2.0
		back().location.z := width / -2.0
		top().location.y := height / 2.0
		bottom().location.y := height / -2.0
		left().location.x := width / -2.0
		right().location.x := width / 2.0
	}
}
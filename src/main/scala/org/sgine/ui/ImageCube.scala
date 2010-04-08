package org.sgine.ui

import org.sgine.core.Resource

class ImageCube extends Cube[Image] {
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
	
	protected def createComponent() = new Image()
}
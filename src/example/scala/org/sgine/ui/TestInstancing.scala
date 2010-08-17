package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestInstancing extends StandardDisplay with Debug {
	def setup() = {
		val image = new Image(Resource("puppies.jpg"))
		scene += image
		
		val i1 = ComponentInstance(image)
		i1.location.set(-300.0, 200.0, 5.0)
		i1.scale.set(0.4)
		i1.alpha := 0.5
		i1.color := Color.Red
		scene += i1
		
		val i2 = ComponentInstance(image)
		i2.location.set(300.0, 200.0, 5.0)
		i2.scale.set(0.4)
		i2.alpha := 0.5
		i2.color := Color.Green
		scene += i2
		
		val i3 = ComponentInstance(image)
		i3.location.set(-300.0, -200.0, 5.0)
		i3.scale.set(0.4)
		i3.alpha := 0.5
		i3.color := Color.Blue
		scene += i3
		
		val i4 = ComponentInstance(image)
		i4.location.set(300.0, -200.0, 5.0)
		i4.scale.set(0.4)
		i4.alpha := 0.5
		scene += i4
	}
}
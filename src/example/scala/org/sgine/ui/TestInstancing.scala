package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestInstancing extends StandardDisplay with Debug {
	def setup() = {
		val manager = new InstanceManager()
		scene += manager
		
		val image = new Image(Resource("puppies.jpg"))
		manager.register("puppies", image)
		
		val i0 = manager.request("puppies")
		scene += i0
		
		val i1 = manager.request("puppies")
		i1.location(-300.0, 200.0, 5.0)
		i1.scale(0.4)
		i1.alpha := 0.5
		i1.color := Color.Red
		scene += i1
		
		val i2 = manager.request("puppies")
		i2.location(300.0, 200.0, 5.0)
		i2.scale(0.4)
		i2.alpha := 0.5
		i2.color := Color.Green
		scene += i2
		
		val i3 = manager.request("puppies")
		i3.location(-300.0, -200.0, 5.0)
		i3.scale(0.4)
		i3.alpha := 0.5
		i3.color := Color.Blue
		scene += i3
		
		val i4 = manager.request("puppies")
		i4.location(300.0, -200.0, 5.0)
		i4.scale(0.4)
		i4.alpha := 0.5
		scene += i4
	}
}
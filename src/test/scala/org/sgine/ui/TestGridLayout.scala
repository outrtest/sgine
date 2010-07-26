package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import org.sgine.ui.layout.GridLayout

object TestGridLayout extends StandardDisplay with Debug {
	def setup() = {
		val container = new Container()
		container.scale.set(0.5)
		val layout = GridLayout(3, 3, 0, 256.0, 256.0)
		container.layout := layout
		
		val i1 = new Image(Resource("sgine_256.png"))
		layout(i1, 0, 0)
		container += i1
		
		val i2 = ComponentInstance(i1)
		i2.color := Color.Red
		layout(i2, 1, 1)
		container += i2
		
		val i3 = ComponentInstance(i1)
		i3.color := Color.Green
		layout(i3, 2, 2)
		container += i3
		
		scene += container
	}
}
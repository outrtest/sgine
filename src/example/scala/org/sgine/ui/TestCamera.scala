package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import simplex3d.math.doublem.renamed._

object TestCamera extends StandardDisplay with Debug {
	def setup() = {
		val container = new Container()
		scene += container
		
		val node = new Box()
		node.visible := false
		node.location.z := 750.0
		container += node
		
		val box = new Box()
		box.source := Resource("sgine_256.png")
		scene += box
		
		val c1 = new ComponentInstance(box)
		c1.location.x := -250.0
		c1.scale(0.2)
		c1.color := Color.Green
		c1.alpha := 0.5
		scene += c1
		
		val c2 = new ComponentInstance(box)
		c2.location.x := 250.0
		c2.scale(0.2)
		c2.color := Color.Blue
		c2.alpha := 0.5
		scene += c2
		
		container.rotation.y.animator = new org.sgine.property.animate.LinearNumericAnimator(1.5)
		container.rotation.y := Double.MaxValue
		
		renderer.camera.link(node)
	}
}
package org.sgine.ui

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import org.sgine.scene.Node
import org.sgine.scene.ext.FocusableNode

object TestFocus extends StandardDisplay with Debug {
	def setup() = {
		val component1 = new Button()
		component1.text := "Button 1"
		component1.location.y := 100.0
		scene += component1
		
		val component2 = new Button()
		component2.text := "Button 2"
		component2.focused := true
		scene += component2
		
		val component3 = new Button()
		component3.text := "Button 3"
		component3.location.y := -100.0
		scene += component3
	}
	
	private def test(n: Node) = n match {
		case f: FocusableNode => true
		case _ => false
	}
}
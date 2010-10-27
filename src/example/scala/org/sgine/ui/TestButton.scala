package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestButton extends StandardDisplay with Debug {
	def setup() = {
		val component = new Button()
		component.text := "Test Button"
		component.size.width.mode := "explicit"
		component.size.height.mode := "explicit"
		component.size(300.0, 300.0)
		scene += component
	}
}
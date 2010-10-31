package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestButton extends StandardDisplay with Debug {
	def setup() = {
		val component = new Button()
		component.text := "Test Button"
		component.focused := true
		scene += component
	}
}
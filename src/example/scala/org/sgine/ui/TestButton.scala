package org.sgine.ui

import org.sgine.render.StandardDisplay

object TestButton extends StandardDisplay {
	def setup() = {
		val component = new Button()
		component.text := "Test Button"
		scene += component
	}
}
package org.sgine.ui

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestTextInput extends StandardDisplay with Debug {
	def setup() = {
		val component = new TextInput()
		component.text := "Test TextInput"
		scene += component
	}
}
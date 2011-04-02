package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay
import org.sgine.render.font.FontManager

object TestTextInput extends StandardDisplay with Debug {
	def setup() = {
		var y = 150.0
		
		var ti: TextInput = null
		
		for (i <- 0 until 5) {
			val label = new Label()
			label.font := FontManager("Arial", 32.0)
			label.text := "Test " + (i + 1) + ":"
			label.location.x.align := "right"
			label.location.x := -5.0
			label.location.y := y
			scene += label
			
			val component = new TextInput()
			component.location.x.align := "left"
			component.location.x := 5.0
			component.location.y := y
			component.text := "TextInput"
//			component.stateFocused("skin().source") = Resource("scale9/windows/textinput/focused.png")
			scene += component
			
			if (i == 4) {
				ti = component
			}
			
			y -= 50.0
		}
	}
}
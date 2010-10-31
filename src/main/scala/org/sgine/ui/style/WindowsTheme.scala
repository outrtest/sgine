package org.sgine.ui.style

import org.sgine.core._

import org.sgine.property.style._

import org.sgine.render.font.FontManager

import org.sgine.ui.TextInput
import org.sgine.ui.skin.Scale9Skin

object WindowsTheme extends Theme {
	val button = new SelectorStyle("Button") {
		val size_width_mode = SizeMode.Auto
		val size_height_mode = SizeMode.Auto
		val textAlignment = HorizontalAlignment.Center
		val font = FontManager("Arial", 24.0)
		val textColor = Color.Black
		val padding_top = 10.0
		val padding_left = 25.0
		val padding_bottom = 10.0
		val padding_right = 25.0
		
		val skin = () => {
			val scale9 = new Scale9Skin()
			scale9(org.sgine.core.Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
			scale9
		}
		val states_focus = "skin().source" -> Resource("scale9/windows/button/focused.png")
		val states_hover = "skin().source" -> Resource("scale9/windows/button/hover.png")
		val states_press = "skin().source" -> Resource("scale9/windows/button/pressed.png")
	}
	register(button)
	
	val textInput = new SelectorStyle("TextInput") {
		val size_width = 300.0
		val size_width_mode = SizeMode.Explicit
		val editable = true
		val textAlignment = HorizontalAlignment.Left
		val font = FontManager("Arial", 24.0)
		val textColor = Color.Black
		val caret_color = Color.Black
		val clip_enabled = true
		val clip_x1 = -150.0
		val clip_x2 = 150.0
		val clip_y1 = -150.0
		val clip_y2 = 150.0
		val padding_top = 5.0
		val padding_left = 5.0
		val padding_bottom = 5.0
		val padding_right = 5.0
		
		val skin = () => {
			val scale9 = new Scale9Skin()
			scale9(Resource("scale9/windows/textinput/normal.png"), 2.0, 2.0, 3.0, 3.0)
			scale9
		}
		val states_focus = "skin().source" -> Resource("scale9/windows/textinput/focused.png")
		val states_hover = "skin().source" -> Resource("scale9/windows/textinput/hover.png")
	}
	register(textInput)
}
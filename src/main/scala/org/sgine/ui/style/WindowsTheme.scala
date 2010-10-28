package org.sgine.ui.style

import org.sgine.core._

import org.sgine.property.style._

import org.sgine.render.font.FontManager

import org.sgine.ui.TextInput
import org.sgine.ui.skin.Scale9Skin

object WindowsTheme extends Theme {
	val textInput = new SelectorStyle("TextInput") {
		val size_width = 300.0
		val size_width_mode = SizeMode.Explicit
		val multiline = false
		val editable = true
		val textAlignment = HorizontalAlignment.Left
		val font = FontManager("Arial", 24.0)
		val textColor = Color.Blue
		val caret_color = Color.Black
		val clip_enabled = true
		val clip_x1 = -150.0
		val clip_x2 = 150.0
		val clip_y1 = -200.0
		val clip_y2 = 200.0
		val padding_top = 5.0
		val padding_left = 5.0
		val padding_bottom = 5.0
		val padding_right = 5.0
		
		override def apply(stylized: Stylized) = {
			super.apply(stylized)
			
			println("Applying textinput to: " + stylized)
			// TODO: handle this better
			stylized match {
				case ti: TextInput => {
					val scale9 = new Scale9Skin()
					scale9(Resource("scale9/windows/textinput/normal.png"), 2.0, 2.0, 3.0, 3.0)
					ti.skin := scale9
				}
			}
		}
	}
//	textInput("size.width") = 300.0
	register(textInput)
	
	/*def apply() = {
		Theme.button.normalSkin := Resource("scale9/windows/button/normal.png")
		Theme.button.hoverSkin := Resource("scale9/windows/button/hover.png")
		Theme.button.pressedSkin := Resource("scale9/windows/button/pressed.png")
		Theme.button.focusedSkin := Resource("scale9/windows/button/focused.png")
		Theme.button.skinX1 := 3.0
		Theme.button.skinY1 := 3.0
		Theme.button.skinX2 := 4.0
		Theme.button.skinY2 := 5.0
		Theme.button.paddingTop := 10.0
		Theme.button.paddingBottom := 10.0
		Theme.button.paddingLeft := 25.0
		Theme.button.paddingRight := 25.0
		Theme.button.iconPlacement := Placement.Top
		
		Theme.font := FontManager("Arial", 24.0)
		Theme.textColor := Color.Black
	}*/
}
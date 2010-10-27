package org.sgine.ui.style

import org.sgine.core._

import org.sgine.property.style._

import org.sgine.render.font.FontManager

object WindowsTheme extends Theme {
	val textInput = new SelectorStyle("TextInput") {
		val size = new SelectorStyle("Size") {
			val width = 300.0
		}
		
		override def apply(stylized: Stylized) = {
			super.apply(stylized)
			
			println("Applying textinput to: " + stylized)
		}
	}
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
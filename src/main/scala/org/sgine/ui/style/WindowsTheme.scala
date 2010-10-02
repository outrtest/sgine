package org.sgine.ui.style

import org.sgine.core.Color
import org.sgine.core.Placement
import org.sgine.core.Resource

import org.sgine.render.font.FontManager

object WindowsTheme {
	def apply() = {
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
		
		Theme.textInput.normalSkin := Resource("scale9/windows/textinput/normal.png")
		Theme.textInput.hoverSkin := Resource("scale9/windows/textinput/hover.png")
		Theme.textInput.focusedSkin := Resource("scale9/windows/textinput/focused.png")
		Theme.textInput.skinX1 := 2.0
		Theme.textInput.skinY1 := 2.0
		Theme.textInput.skinX2 := 3.0
		Theme.textInput.skinY2 := 3.0
		Theme.textInput.paddingTop := 5.0
		Theme.textInput.paddingBottom := 5.0
		Theme.textInput.paddingLeft := 5.0
		Theme.textInput.paddingRight := 5.0
		
		Theme.font := FontManager("Arial32")
		Theme.textColor := Color.Black
	}
}
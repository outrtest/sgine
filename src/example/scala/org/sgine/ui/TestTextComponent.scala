package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay
import org.sgine.render.font.FontManager

object TestTextComponent extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val text = new TextComponent()
		
//		text.clip.enabled := true
		text.clip.x1 := -250.0
		text.clip.y1 := -150.0
		text.clip.x2 := 250.0
		text.clip.y2 := 150.0
		
		text._font := FontManager("Arial", 64.0)
		text.focused := true
		text._multiline := true
		text._editable := true
		text._textAlignment := "left"
		text._text := "Now is the time for all good men to come to the aid of their country.\n\nCharles E. Weller"
		text.size.width := 600.0
//		text.rotation.y := 1.0
		scene += text
	}
}
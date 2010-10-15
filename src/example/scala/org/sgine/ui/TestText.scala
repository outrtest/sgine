package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay
import org.sgine.render.font.FontManager

object TestText extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val text = new Text()
		
//		text.clip.enabled := true
		text.clip.x1 := -150.0
		text.clip.y1 := 150.0
		text.clip.x2 := 150.0
		text.clip.y2 := -150.0
		
		text.font := FontManager("Arial", 64.0)
		text.focused := true
		text.editable := true
		text.textAlignment := "left"
		text.text := "Now\nis\nthe time for all good men to come to the aid of their country."
		text.size.width := 600.0
//		text.rotation.y := 1.0
		scene += text
		
		val box = new Box()
		box.size(300.0, 300.0, 0.1)
		box.color := Color.Red
		box.location.z := -1.0
		scene += box
	}
}
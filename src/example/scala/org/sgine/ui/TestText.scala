package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.RenderSettings
import org.sgine.render.StandardDisplay

object TestText extends StandardDisplay with Debug {
	override val settings = RenderSettings.High
	
	def setup() = {
		val text = new Text()
		text.focused := true
		text.editable := true
		text.textAlignment := "right"
		text.text := "Now\nis\nthe time for all good men to come to the aid of their country."
		text.size.width := 300.0
		scene += text
	}
}
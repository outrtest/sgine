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
		text.horizontalAlignment := "left"
		text.text := "Now\nis\nthe time for all good men to come to the aid of their country."
//		text.text := "Wahoo!"
		text.size.width := 300.0
//		text.caret.position := 5
//		text.selection(5, 10)
//		text.selection.all()
		println("Selected: " + text.selection.text)
		scene += text
		
//		println("Focused: " + org.sgine.scene.ext.FocusableNode.focused())
//		Thread.sleep(5000)
//		text.text := "Wahoo!"
	}
}
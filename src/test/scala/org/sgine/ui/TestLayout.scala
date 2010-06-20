package org.sgine.ui

import org.sgine.core._

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import org.sgine.ui.layout.BoxLayout

object TestLayout extends StandardDisplay with Debug {
	def setup() = {
		val container = new Container()
		container.layout := BoxLayout("vertical", 10)
		container += new Button("Test Button 1")
		container += new Button("Test Button 2")
		scene += container
	}
}
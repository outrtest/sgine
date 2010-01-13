package org.sgine.visual.renderer

import java.awt.Container

trait Renderer {
	def init(awtContainer: Container): Unit
}

object Renderer {
	var Default: Renderer = _
}
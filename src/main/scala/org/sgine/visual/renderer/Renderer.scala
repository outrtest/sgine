package org.sgine.visual.renderer

import org.sgine.visual.Window
import org.sgine.visual.renderer.lwjgl.LWJGLRenderer

import java.awt.Container

trait Renderer {
	def init(window: Window): Unit
}

object Renderer {
	var Default: Renderer = LWJGLRenderer
}
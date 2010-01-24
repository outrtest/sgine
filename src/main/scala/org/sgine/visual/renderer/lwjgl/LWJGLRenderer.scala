package org.sgine.visual.renderer.lwjgl

import java.awt.Container

import org.sgine.visual.Window
import org.sgine.visual.renderer.Renderer

object LWJGLRenderer extends Renderer {
	def init(window: Window) = {
		new LWJGLRendererInstance(window)
	}
}
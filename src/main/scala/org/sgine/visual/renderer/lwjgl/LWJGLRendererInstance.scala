package org.sgine.visual.renderer.lwjgl

import org.sgine.opengl.FPS
import org.sgine.opengl.GLContainer

import org.sgine.visual.Window

class LWJGLRendererInstance (window: Window) {
	val glContainer = GLContainer(window.awtContainer())
	
	glContainer.displayables.add(FPS())
	
	glContainer.begin()
	
	def shutdown() = {
		// TODO: support shutting down gracefully
		System.exit(0)
	}
}
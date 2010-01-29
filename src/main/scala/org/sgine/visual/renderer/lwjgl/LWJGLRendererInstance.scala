package org.sgine.visual.renderer.lwjgl

import org.sgine.opengl.FPS
import org.sgine.opengl.GLContainer

import org.sgine.visual.Window

class LWJGLRendererInstance (window: Window) {
	val glWindow = GLContainer(window.awtContainer())
	
	glWindow.displayables.add(FPS())
	
	glWindow.begin()
}
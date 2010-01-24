package org.sgine.visual.renderer.lwjgl

import org.sgine.opengl.FPS
import org.sgine.opengl.GLWindow

import org.sgine.visual.Window

class LWJGLRendererInstance (window: Window) {
	// TODO: re-evaluate GLWindow - better GL abstraction layer needed here that is also useful outside of visual
	val glWindow = GLWindow(title = window.title(), width = 1024, height = 768, frame = window.awtContainer().asInstanceOf[java.awt.Frame])
	
	glWindow.displayables.add(FPS())
	
	glWindow.begin()
}
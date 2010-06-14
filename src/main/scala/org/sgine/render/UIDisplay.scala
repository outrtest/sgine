package org.sgine.render

import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

trait UIDisplay {
	val Applet = 1
	val Frame = 2
	
	private var _renderer: Renderer = _
	
	def renderer = _renderer
	val scene = new GeneralNodeContainer() with ResolutionNode
	
	initialize()
	
	protected def initialize() = {
		scene.setResolution(1024, 768)
	}
	
	def start(title: String, mode: Int = Frame, width: Int = 1024, height: Int = 768) = {
		if (mode == Frame) {
			_renderer = Renderer.createFrame(width, height, title)
			renderer.renderable := RenderableScene(scene)
		}
	}
}
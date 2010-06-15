package org.sgine.render

import org.sgine.event.EventHandler
import org.sgine.event.Listenable
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeDelegate

import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

trait StandardDisplay extends Listenable {
	private var _renderer: Renderer = _
	
	def renderer = _renderer
	val scene = new GeneralNodeContainer() with ResolutionNode
	val title = new AdvancedProperty[String](generateTitle(), this)
	
	private def generateTitle() = {
		val name = getClass.getSimpleName
		name.substring(0, name.length - 1)
	}
	
	initialize()
	
	protected def initialize() = {
		scene.setResolution(1024, 768)
	}
	
	def setup(): Unit
	
	def start(mode: WindowMode = WindowMode.Frame, width: Int = 1024, height: Int = 768) = {
		if (mode == WindowMode.Frame) {
			_renderer = Renderer.createFrame(width, height, title())
			renderer.renderable := RenderableScene(scene)
			
			_renderer.canvas.getParent match {
				case f: java.awt.Frame => title.listeners += EventHandler(PropertyChangeDelegate(f.setTitle), ProcessingMode.Blocking)
				case _ =>
			}
		} else {
			error("Unsupported WindowMode: " + mode)
		}
	}
}
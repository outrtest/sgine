package org.sgine.render

import org.sgine.event.EventHandler
import org.sgine.event.Listenable
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeDelegate

import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

/**
 * StandardDisplay provides a convenience trait that
 * can be inherited to seamlessly provide support for
 * Applet, Canvas, and Frame display of applications.
 * 
 * The only requirement for a working application is
 * to extend this trait with an object and implement
 * the "setup" method.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait StandardDisplay extends Listenable {
	private var _renderer: Renderer = _
	
	/**
	 * Reference to the Renderer
	 * 
	 * @return
	 * 		Renderer
	 */
	def renderer = _renderer
	
	/**
	 * Reference to the root NodeContainer with
	 * ResolutionNode mixed-in.
	 * 
	 * @return
	 * 		GeneralNodeContainer with ResolutionNode
	 */
	val scene = new GeneralNodeContainer() with ResolutionNode
	
	/**
	 * The title if in a framed context.
	 * 
	 * @return
	 * 		title property
	 */
	val title = new AdvancedProperty[String](generateTitle(), this)
	
	private def generateTitle() = {
		val name = getClass.getSimpleName
		name.substring(0, name.length - 1)
	}
	
	initialize()
	
	protected def initialize() = {
		scene.setResolution(1024, 768)
		
		setup()
	}
	
	/**
	 * Invoked upon initialization of the display. This method
	 * should be implemented to set up the scene.
	 */
	def setup(): Unit
	
	/**
	 * Invoked to start the display. This is invoked with default arguments
	 * in the default "main" method.
	 * 
	 * @param mode
	 * 			The display WindowMode. Defaults to WindowMode.Frame.
	 * @param width
	 * 			Initial window width. Defaults to 1024.
	 * @param height
	 * 			Initial window height. Defaults to 768.
	 */
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
	
	/**
	 * Default implementation of main method invoking start() with
	 * default arguments.
	 * 
	 * @param args
	 */
	def main(args: Array[String]): Unit = {
		start()
	}
}
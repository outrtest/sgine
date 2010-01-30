package org.sgine.visual

import renderer.Renderer

import java.applet.Applet
import java.awt.Container

class Application extends Applet {
	val window = Window("sgine application", renderer = pickRenderer)
	
	/**
	 * Overrides the Applet.init to initialize the display
	 * for applets.
	 */
	override final def init = {			// Applet.init
		super.init
		
		window.start(this)
	}
	
	/**
	 * Called during initialization to determine the Renderer instance that
	 * should be utilized for this Application instance. By default this
	 * makes a call to Renderer.Default.
	 * 
	 * @return
	 * 		org.sgine.visual.renderer.Renderer
	 */
	protected def pickRenderer = Renderer.Default
}
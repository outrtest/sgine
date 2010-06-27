package org.sgine.render

import org.sgine.scene.MutableNodeContainer

trait Display {
	/**
	 * Reference to the Renderer
	 * 
	 * @return
	 * 		Renderer
	 */
	def renderer: Renderer
	/**
	 * Reference to the root NodeContainer with
	 * ResolutionNode mixed-in.
	 * 
	 * @return
	 * 		GeneralNodeContainer with ResolutionNode
	 */
	def scene: MutableNodeContainer
	
	protected def init() = {
	}
	
	/**
	 * Invoked upon initialization of the display. This method
	 * should be implemented to set up the scene.
	 */
	def setup(): Unit
}
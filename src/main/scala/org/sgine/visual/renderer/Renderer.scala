package org.sgine.visual.renderer

import org.sgine.visual.Window
import org.sgine.visual.renderer.lwjgl.LWJGLRenderer

import java.awt.Container

trait Renderer {
	/**
	 * Initialize the Renderer
	 */
	def init(window: Window): Unit
	
	/**
	 * Pass control off to the Renderer to begin display
	 */
	def start(window: Window): Unit
	
	/**
	 * Shutdown the renderer and cleanup
	 */
	def shutdown(window: Window): Boolean
}

object Renderer {
	var Default: Renderer = LWJGLRenderer
}
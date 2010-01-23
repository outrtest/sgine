package org.sgine.visual

import org.sgine.property._
import org.sgine.property.container._
import org.sgine.scene._
import org.sgine.scene.query._
import org.sgine.scene.view._
import org.sgine.visual.renderer._

class Window private() extends PropertyContainer {
	val title = new AdvancedProperty[String](null, this)
	val width = new AdvancedProperty[Int](0, this)
	val height = new AdvancedProperty[Int](0, this)
	val scene = new AdvancedProperty[MutableNodeContainer](null, this)
	val updateView = new DelegateProperty[NodeView](() => _updateView)
	val shapesView = new DelegateProperty[NodeView](() => _shapesView)
	/**
	 * The java.awt.Container this Window is using to display.
	 * This may be null if running in headless mode.
	 * 
	 * @return
	 * 		java.awt.Container
	 */
	val awtContainer = new DelegateProperty[java.awt.Container](() => _awtContainer)
	/**
	 * The Renderer this Window instance is using to render.
	 */
	val renderer = new DelegateProperty[Renderer](() => _renderer)
	
	private var _updateView: NodeView = _
	private var _shapesView: NodeView = _
	private var _awtContainer: java.awt.Container = _
	private var _renderer: Renderer = _
	
	def reload() = {
		// Register NodeView for updates
		
		
		// Register NodeView for Shapes
	}
	
	def start(awtContainer: java.awt.Container = new java.awt.Frame(), renderer: Renderer = Renderer.Default) = {
		// Assign container
		_awtContainer = awtContainer
		width := awtContainer.getWidth()
		height := awtContainer.getHeight()
		
		// Assign renderer
		_renderer = renderer
		
		// Pass control to Renderer
		renderer.init(this)
	}
}

object Window {
	def apply(title: String, scene: MutableNodeContainer = new GeneralNodeContainer): Window = {
		val w = new Window()
		
		// Set properties
		w.title := title
		w.scene := scene
		
		w.reload()
		
		w
	}
}
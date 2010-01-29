package org.sgine.visual

import org.sgine.property._
import org.sgine.property.container._
import org.sgine.scene._
import org.sgine.scene.query._
import org.sgine.scene.view._
import org.sgine.visual.awt.AWTFrame
import org.sgine.visual.renderer._
import org.sgine.visual.scene.ShapeQuery

class Window private() extends PropertyContainer {
	/**
	 * The title of this Window
	 */
	val title = new AdvancedProperty[String](null, this)
	/**
	 * The width of this Window
	 */
	val width = new AdvancedProperty[Int](0, this)
	/**
	 * The height of this Window
	 */
	val height = new AdvancedProperty[Int](0, this)
	/**
	 * The scene this Window renders
	 */
	val scene = new AdvancedProperty[MutableNodeContainer](null, this)
	/**
	 * A read-only NodeView for the Updatable Nodes in the scene
	 */
	val updateView = new DelegateProperty[NodeView](() => _updateView)
	/**
	 * A read-only NodeView for the ShapeNodes in the scene
	 */
	val shapesView = new DelegateProperty[NodeView](() => _shapesView)
	/**
	 * The java.awt.Container this Window is using to display.
	 * This may be null if running in headless mode.
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
		_updateView = NodeView(scene(), UpdatablesQuery, false)
		
		// Register NodeView for Shapes
		_shapesView = NodeView(scene(), ShapeQuery, false)
	}
	
	def start(awtContainer: java.awt.Container = new AWTFrame(this), renderer: Renderer = Renderer.Default) = {
		// Assign container
		_awtContainer = awtContainer
		width := awtContainer.getWidth()
		height := awtContainer.getHeight()
		
		// Assign renderer
		_renderer = renderer
		
		// Pass control to Renderer
		renderer.init(this)
	}
	
	def stop() = {
		// TODO: support shutting down gracefully
		System.exit(0)
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
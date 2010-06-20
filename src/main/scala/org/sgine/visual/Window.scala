package org.sgine.visual

import org.sgine.scene.view.event.NodeRemovedEvent
import org.sgine.scene.view.event.NodeAddedEvent
import org.sgine.scene.event.NodeContainerEvent
import org.sgine._
import org.sgine.visual.time.RealtimeTimer
import org.sgine.visual.time.Timer
import org.sgine.event._
import org.sgine.property._
import org.sgine.property.container._
import org.sgine.property.event._
import org.sgine.scene._
import org.sgine.scene.query._
import org.sgine.scene.view._
import org.sgine.visual.awt.AWTFrame
import org.sgine.visual.renderer._
import org.sgine.visual.scene.ShapeQuery
import org.sgine.work._

class Window private(_renderer: Renderer) extends PropertyContainer with Listenable {
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
	val shapesView = new DelegateProperty[NodeView](_shapesView.apply)
	/**
	 * The java.awt.Container this Window is using to display.
	 * This may be null if running in headless mode.
	 */
	val awtContainer = new DelegateProperty[java.awt.Container](() => _awtContainer)
	/**
	 * The Renderer this Window instance is using to render.
	 */
	val renderer = new DelegateProperty[Renderer](() => _renderer)
	/**
	 * The Timer used for updates to updatables. Defaults to RealtimeTimer.
	 */
	val updateTimer = new DelegateProperty[Timer](() => _updateTimer)
	
	private var _updateView: NodeView = _
	private var _shapesView = new AdvancedProperty[NodeView](null, this, "shapesView")
	private var _awtContainer: java.awt.Container = _
	private var _updateTimer: Timer = _
	
	// Add listener to know when a new scene has been set
	scene.listeners += EventHandler(sceneChanged _, processingMode = ProcessingMode.Blocking)
	
	def reload() = {
		// Register NodeView for Shapes
		_shapesView := NodeView(scene(), ShapeQuery, false)
	}
	
	def start(awtContainer: java.awt.Container = new AWTFrame(this), updateTimer: Timer = new RealtimeTimer) = {
		// Assign container
		_awtContainer = awtContainer
		width := awtContainer.getWidth()
		height := awtContainer.getHeight()
		
		// Pass control to Renderer
		renderer().start(this)
		
		// Initialize timer
		_updateTimer = updateTimer
//		DefaultWorkManager += Updater(updateTimer)
	}
	
	def stop() = {
		renderer().shutdown(this)
	}
	
	private def sceneChanged(e: PropertyChangeEvent[MutableNodeContainer]) = {
		reload()
	}
}

object Window {
	def apply(title: String, scene: MutableNodeContainer = new GeneralNodeContainer, renderer: Renderer = Renderer.Default): Window = {
		val w = new Window(renderer)
		w.renderer().init(w)
		
		// Set properties
		w.title := title
		w.scene := scene
		
		w
	}
}
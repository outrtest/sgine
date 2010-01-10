package org.sgine.visual

import org.sgine.property._
import org.sgine.property.container._
import org.sgine.scene._
import org.sgine.scene.query._
import org.sgine.scene.view._

class Window private() extends PropertyContainer {
	val title = new AdvancedProperty[String](null, this)
	val width = new AdvancedProperty[Int](0, this)
	val height = new AdvancedProperty[Int](0, this)
	val scene = new AdvancedProperty[MutableNodeContainer](null, this)
	def updateView = _updateView
	def shapesView = _shapesView
	
	private var _updateView: NodeView = _
	private var _shapesView: NodeView = _
	
	def reload() = {
		// Register NodeView for updates
		
		
		// Register NodeView for Shapes
	}
}

object Window {
	def apply(title: String, width: Int = 800, height: Int = 600, scene: MutableNodeContainer = new GeneralNodeContainer): Window = {
		val w = new Window()
		
		// Set properties
		w.title := title
		w.width := width
		w.height := height
		w.scene := scene
		
		w.reload()
		
		w
	}
}
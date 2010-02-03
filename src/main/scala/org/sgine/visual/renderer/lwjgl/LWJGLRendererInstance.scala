package org.sgine.visual.renderer.lwjgl

import org.sgine.math._
import org.sgine.opengl.GLWindow
import org.sgine.visual.Shape
import org.sgine.scene.view.event.NodeAddedEvent
import org.sgine.property._
import org.sgine.event._

import org.sgine.opengl.FPS
import org.sgine.opengl.GLContainer
import org.sgine.opengl.shape.{Shape => GLShape}
import org.sgine.opengl.state._

import org.sgine.visual.Window

import org.lwjgl.opengl.GL11._;

class LWJGLRendererInstance (window: Window) {
	lazy val glContainer = GLContainer()
	glContainer.displayables.add(TranslateState(0.0, 0.0, -1000.0));		// TODO: use camera instead
	
	glContainer.containerWidth bind window.width
	glContainer.containerHeight bind window.height
	window.listeners += EventHandler(test _, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	window.listeners += EventHandler(propertyChanged, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	window.listeners += EventHandler(nodeAdded, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	private var shapes: List[Shape] = Nil
	
	def start() = {
		glContainer.displayables.add(FPS())
		glContainer.begin(window.awtContainer())
	}
	
	private def test(e: Event) = {
		e match {
			case pce: PropertyChangeEvent[_] =>
			case nae: NodeAddedEvent =>
			case _ => println("LWJGLRendererInstance: " + e)
		}
	}
	
	private def propertyChanged(evt: PropertyChangeEvent[_]) = {
		evt.property match {
			case np: NamedProperty => println(np.name + ": " + evt.oldValue + " -> " + evt.newValue)
			case _ => println(evt.oldValue + " -> " + evt.newValue)
		}
	}
	
	private def nodeAdded(evt: NodeAddedEvent) = {
		val s = evt.node.asInstanceOf[Shape]
		val m = s.mesh()
		for (v <- m.vertices) {
			println("vertice: " + v)
		}
		// TODO: this should be triangles
		val gls = GLShape(
			GL_QUADS,
			m.vertices: _*
		);
		// TODO: these should reflect the coordinates associated with the Shape
		gls.textureCoordinates(0) = Vector2.UnitY;
		gls.textureCoordinates(1) = Vector2.Ones;
		gls.textureCoordinates(2) = Vector2.UnitX;
		gls.textureCoordinates(3) = Vector2.Zero;
		glContainer.displayables.add(gls)
	}
	
	def shutdown() = {
		// TODO: support shutting down gracefully
		System.exit(0)
	}
}
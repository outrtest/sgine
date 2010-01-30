package org.sgine.visual.renderer.lwjgl

import org.sgine.property._
import org.sgine.event._

import org.sgine.opengl.FPS
import org.sgine.opengl.GLContainer

import org.sgine.visual.Window

class LWJGLRendererInstance (window: Window) {
	lazy val glContainer = GLContainer(window.awtContainer())
	
//	window.listeners += EventHandler(test _, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	window.listeners += EventHandler(propertyChanged, processingMode = ProcessingMode.Blocking , recursion = Recursion.Children)
	
	def start() = {
		glContainer.displayables.add(FPS())
		glContainer.begin()
	}
	
	private def test(e: Event) = {
		e match {
			case pce: PropertyChangeEvent[_] => {
				println(pce.oldValue + " -> " + pce.newValue)
			}
			case _ => println("LWJGLRendererInstance: " + e)
		}
	}
	
	private def propertyChanged(evt: PropertyChangeEvent[_]) = {
		evt.property match {
			case np: NamedProperty => println(np.name + ": " + evt.oldValue + " -> " + evt.newValue)
			case _ => println(evt.oldValue + " -> " + evt.newValue)
		}
	}
	
	def shutdown() = {
		// TODO: support shutting down gracefully
		System.exit(0)
	}
}
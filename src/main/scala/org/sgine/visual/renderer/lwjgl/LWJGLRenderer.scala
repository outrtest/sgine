package org.sgine.visual.renderer.lwjgl

import scala.collection.mutable.HashMap
import java.awt.Container

import org.sgine.visual.Window
import org.sgine.visual.renderer.Renderer

object LWJGLRenderer extends Renderer {
	private val map = new HashMap[Window, LWJGLRendererInstance]
	
	def init(window: Window) = {
		val r = new LWJGLRendererInstance(window)
		map.put(window, r)
	}
	
	def start(window: Window) = {
		map.get(window) match {
			case Some(r) => r.start()
			case None => throw new RuntimeException("Unable to start Window because renderer instance was not found for LWJGLRenderer")
		}
	}
	
	def shutdown(window: Window) = {
		map.remove(window) match {
			case Some(r) => {
				r.shutdown()
				true
			}
			case None => false
		}
	}
}
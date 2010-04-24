package org.sgine.render.scene

import org.sgine.bounding.BoundingObject

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.Mouse
import org.sgine.input.event.MouseEvent

import org.sgine.math.mutable.MatrixPropertyContainer
import org.sgine.math.mutable.Vector3

import org.sgine.render.FPS
import org.sgine.render.Renderable
import org.sgine.render.Renderer

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer
import org.sgine.scene.view.NodeView

class RenderableScene private(val scene: NodeContainer, val showFPS: Boolean) extends Renderable {
	val view = NodeView(scene, RenderableQuery, false)
	val fps = FPS()
	
	private val storeVector3 = Vector3()
	
	private var initted = false
	private var renderer: Renderer = _
	
	private def init() = {
		initted = true
		
		Mouse.listeners += EventHandler(mouseEvent, ProcessingMode.Blocking)
	}
	
	def render(renderer: Renderer) = {
		this.renderer = renderer
		
		if (!initted) init()
		
		view.foreach(renderItem)
		if (showFPS) fps()
	}
	
	private val renderItem = (n: Node) => n.asInstanceOf[Renderable].render(renderer)
	
	private var currentMouseEvent: MouseEvent = null
	private def mouseEvent(evt: MouseEvent) = {
		currentMouseEvent = evt
		view.foreach(pickTest)
	}
	
	private val pickTest = (n: Node) => {
		n match {
			case c: MatrixPropertyContainer with BoundingObject => {
				renderer.translateLocal(currentMouseEvent.x, currentMouseEvent.y, c.matrix(), storeVector3)
				if (c.bounding().within(storeVector3)) {
					val evt = currentMouseEvent.retarget(c, storeVector3.x, storeVector3.y)
					Event.enqueue(evt)
				}
			}
			case _ =>
		}
	}
}

object RenderableScene {
	def apply(scene: NodeContainer, showFPS: Boolean = true) = new RenderableScene(scene, showFPS)
}
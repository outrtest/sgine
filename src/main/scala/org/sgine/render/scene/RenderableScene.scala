package org.sgine.render.scene

import org.lwjgl.opengl.GL11._

import org.sgine.bounding.BoundingObject

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.Mouse
import org.sgine.input.event.MouseEvent
import org.sgine.input.event.MouseMoveEvent

import org.sgine.math.mutable.Vector3

import org.sgine.render.FPS
import org.sgine.render.Renderable
import org.sgine.render.Renderer

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer
import org.sgine.scene.ext.MatrixNode
import org.sgine.scene.view.NodeView

class RenderableScene private(val scene: NodeContainer, val showFPS: Boolean) extends Renderable {
	val view = NodeView(scene, RenderableQuery, false)
	view.sortFunction = RenderSort
	val fps = FPS()
	
	private val storeVector3 = Vector3()
	
	private var initted = false
	private var renderer: Renderer = _
	private var itemCount: Float = _
	
	private var _hits: List[Node] = Nil
	private var currentHits: List[Node] = Nil
	
	def hits = _hits
	
	private def init() = {
		initted = true
		
		Mouse.listeners += EventHandler(mouseEvent, ProcessingMode.Blocking)
	}
	
	def render(renderer: Renderer) = {
		this.renderer = renderer
		
		if (!initted) init()

		itemCount = 0.0f
//		view.sort()					// TODO: is this the most efficient way to handle this?
		view.foreach(renderItem)
		if (showFPS) fps()
	}
	
	private val renderItem = (n: Node) => {
		// TODO: this helps z-fighting, but doesn't stop it - test with z-rotated at the same location
		glPolygonOffset(1.0f, itemCount)
		itemCount += 0.000001f
		
		n.asInstanceOf[Renderable].render(renderer)
	}
	
	private var currentMouseEvent: MouseEvent = null
	private def mouseEvent(evt: MouseEvent) = {
		currentMouseEvent = evt
		currentHits = Nil
		view.foreach(pickTest)
		
		// Handle mouseOver and mouseOut
		if (evt.isInstanceOf[MouseMoveEvent]) {
			val move = evt.asInstanceOf[MouseMoveEvent]
			for (n <- currentHits) {
				if (!hits.contains(n)) {		// MouseOver
					translateLocal(move, n)
					val e = MouseEvent(-2, false, 0, storeVector3.x, storeVector3.y, move.deltaX, move.deltaY, n)
					Event.enqueue(e)
				}
			}
			for (n <- hits) {
				if (!currentHits.contains(n)) {	// MouseOut
					translateLocal(move, n)
					val e = MouseEvent(-3, false, 0, storeVector3.x, storeVector3.y, move.deltaX, move.deltaY, n)
					Event.enqueue(e)
				}
			}
			
			_hits = currentHits
		}
	}
	
	private def translateLocal(evt: MouseEvent, n: Node) = {
		val c = n.asInstanceOf[MatrixNode with BoundingObject with Node]
		renderer.translateLocal(evt.x, evt.y, c.worldMatrix(), storeVector3)
		
		c
	}
	
	private val pickTest = (n: Node) => {
		if ((n.isInstanceOf[MatrixNode]) && (n.isInstanceOf[BoundingObject])) {
			val c = translateLocal(currentMouseEvent, n)
//			renderer.translateLocal(currentMouseEvent.x, currentMouseEvent.y, c.matrix(), storeVector3)
			if (c.bounding().within(storeVector3)) {
				val evt = currentMouseEvent.retarget(c, storeVector3.x, storeVector3.y)
				Event.enqueue(evt)
				
				currentHits = c :: currentHits
			}
		}
		// TODO: for some reason the following doesn't work!
//		n match {
//			case c: MatrixPropertyContainer with BoundingObject => {
//				renderer.translateLocal(currentMouseEvent.x, currentMouseEvent.y, c.matrix(), storeVector3)
//				if (c.bounding().within(storeVector3)) {
//					val evt = currentMouseEvent.retarget(c, storeVector3.x, storeVector3.y)
//					Event.enqueue(evt)
//				}
//			}
//			case _ =>
//		}
	}
}

object RenderableScene {
	def apply(scene: NodeContainer, showFPS: Boolean = true) = new RenderableScene(scene, showFPS)
}
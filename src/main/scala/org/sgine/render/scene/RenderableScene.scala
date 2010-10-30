package org.sgine.render.scene

import org.lwjgl.opengl.GL11._

import org.sgine.bounding.BoundingObject

import org.sgine.core.ProcessingMode

import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.input.Mouse
import org.sgine.input.event.MouseEvent
import org.sgine.input.event.MouseMoveEvent

import org.sgine.math.intersection.Ray

import org.sgine.render.FPS
import org.sgine.render.Renderable
import org.sgine.render.Renderer
import org.sgine.render.RenderUpdatable

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer
import org.sgine.scene.ext.MatrixNode
import org.sgine.scene.view.NodeView

import simplex3d.math._
import simplex3d.math.doublem._
import simplex3d.math.doublem.DoubleMath._

class RenderableScene private(val scene: NodeContainer, val showFPS: Boolean) extends Renderable {
	val renderableView = NodeView(scene, RenderableQuery, false)
	renderableView.sortFunction = RenderSort
	val updatableView = NodeView(scene, RenderUpdatableQuery, false)
	updatableView.sortFunction = RenderSort
	val fps = FPS()
	
	private var initted = false
	private var renderer: Renderer = _
	
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

		// TODO: handle sorting function
		updatableView.sort()
		updatableView.foreach(updateItem)
		renderableView.sort()					// TODO: is this the most efficient way to handle this?
		renderableView.foreach(renderItem)
		if (showFPS) fps()
	}
	
	private val renderItem = (n: Node) => {
		Renderable.render(renderer, n.asInstanceOf[Renderable])
	}
	
	private val updateItem = (n: Node) => {
		RenderUpdatable.update(renderer, n.asInstanceOf[RenderUpdatable])
	}
	
	private var currentMouseEvent: MouseEvent = null
	private def mouseEvent(evt: MouseEvent) = {
		currentMouseEvent = evt
		currentHits = Nil
		renderableView.foreach(pickTest)
		
		// Handle mouseOver and mouseOut
		if (evt.isInstanceOf[MouseMoveEvent]) {
			val move = evt.asInstanceOf[MouseMoveEvent]
			for (n <- currentHits) {
				if (!hits.contains(n)) {		// MouseOver
					val v = screenToModelCoords(move, n)
					val e = MouseEvent(-2, false, 0, v.x, v.y, move.deltaX, move.deltaY, n)
					n match {
						case c: org.sgine.ui.Component => c.mouseState.flag.set(Mouse.Over)
						case _ =>
					}
					Event.enqueue(e)
				}
			}
			for (n <- hits) {
				if (!currentHits.contains(n)) {	// MouseOut
					val v = screenToModelCoords(move, n)
					val e = MouseEvent(-3, false, 0, v.x, v.y, move.deltaX, move.deltaY, n)
					n match {
						case c: org.sgine.ui.Component => c.mouseState.flag.remove(Mouse.Over)
						case _ =>
					}
					Event.enqueue(e)
				}
			}
			
			_hits = currentHits
		}
	}
	
//	private def translateLocal(evt: MouseEvent, n: Node) = {
//		val c = n.asInstanceOf[MatrixNode with BoundingObject with Node]
//		renderer.translateLocal(evt.x, evt.y, c.worldMatrix(), storeVector3)
//		
//		c
//	}

	// Coordinates are not sufficient to compute an intersection.
	// Need new ray/model intersection that uses Ray(Vec3(evt.x, evt.y, -1), Vec3(evt.x, evt.y, 1))
	private def screenToModelCoords(evt: MouseEvent, n: Node) :Vec3d = {
		val c = n.asInstanceOf[MatrixNode with BoundingObject with Node]
		val im = inverse(c.worldMatrix())
		im.transformPoint(renderer.screenToWorldCoords(Vec3d(evt.x, evt.y, 0)))
	}
	
	private val pickTest = (n: Node) => {
		if ((n.isInstanceOf[MatrixNode]) && (n.isInstanceOf[BoundingObject])) {
			val c = n.asInstanceOf[MatrixNode with BoundingObject with Node]
			
			val origin = renderer.screenToWorldCoords(Vec3d(currentMouseEvent.x, currentMouseEvent.y, -1))
			val direction = renderer.screenToWorldCoords(Vec3d(currentMouseEvent.x, currentMouseEvent.y, +1)) - origin
			
			val worldToLocalMatrix = inverse(c.worldMatrix())  
					
			val localRay = new Ray(
				worldToLocalMatrix.transformPoint(origin),
				worldToLocalMatrix.transformVector(direction)
			)
			
			val b = c.bounding()
			// this is a hack, the bounding interface c.bound() should be reworked to return min and max.
			val bound = Vec3d(b.width, b.height, -b.depth)/2
			val intersections = localRay.intersectAabb(-bound, +bound)
			
			if (!intersections.isEmpty) {
				val v = localRay.point(intersections(0))
				val evt = currentMouseEvent.retarget(c, v.x, v.y)
				Event.enqueue(evt)
				
				currentHits = c :: currentHits
			}
		}
	}
}

object RenderableScene {
	def apply(scene: NodeContainer, showFPS: Boolean = false) = new RenderableScene(scene, showFPS)
}
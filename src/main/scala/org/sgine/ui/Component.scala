package org.sgine.ui

import org.lwjgl.opengl.GL11._

import org.sgine.bounding.Bounding
import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingBox

import org.sgine.core._

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.Listenable
import org.sgine.event.ProcessingMode
import org.sgine.event.Recursion

import org.sgine.scene.ext.ColorNode
import org.sgine.scene.ext.MatrixNode

import org.sgine.math.MathUtil

import org.sgine.property.AdvancedProperty
import org.sgine.property.DelegateProperty
import org.sgine.property.ImmutableProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.state.Stateful

import org.sgine.render.Renderable
import org.sgine.render.Renderer
import org.sgine.render.RenderUpdatable

import org.sgine.scene.Node
import org.sgine.scene.NodeContainer

import org.sgine.ui.ext._

import simplex3d.math._
import simplex3d.math.doublem._
import simplex3d.math.doublem.DoubleMath._

trait Component extends PropertyContainer with Renderable with RenderUpdatable with MatrixNode with ColorNode with Stateful with BoundingObject {
	val id = new AdvancedProperty[String](null, this)
	val visible = new AdvancedProperty[Boolean](true, this)
	val renderer = new DelegateProperty(() => _renderer)
	val lighting = new AdvancedProperty[Boolean](true, this)
	
	val location = new Location(this)
	val rotation = new Rotation(this)
	val scale = new Scale(this)
	val padding = new Padding(this)
	val dimension = new Dimension(this)
	
	bounding := BoundingBox(0.0, 0.0, 0.0)
	
	private val updateActualLocationHandler = EventHandler(updateActualLocation, processingMode = ProcessingMode.Blocking)
	location.x.listeners += updateActualLocationHandler
	location.y.listeners += updateActualLocationHandler
	location.z.listeners += updateActualLocationHandler
	private val updateActualRotationHandler = EventHandler(updateActualRotation, processingMode = ProcessingMode.Blocking)
	rotation.x.listeners += updateActualRotationHandler
	rotation.y.listeners += updateActualRotationHandler
	rotation.z.listeners += updateActualRotationHandler
	
	bounding.listeners += updateActualLocationHandler
	
	location.actual.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	rotation.actual.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	scale.listeners += EventHandler(invalidateMatrix, processingMode = ProcessingMode.Blocking, recursion = Recursion.Children)
	
	private var firstRender = true
	
	private var _renderer: Renderer = _
	
	def update(renderer: Renderer) = {
	}
	
	def render(renderer: Renderer) = {
		_renderer = renderer
		if (firstRender) {
			firstRender = false
			initComponent()
		}
		
		// Disable lighting if enabled
		if ((!lighting()) && (Renderer().lighting())) {
			glDisable(GL_LIGHTING)
		}
		
		preRender()
		_renderer.loadMatrix(worldMatrix())
		
		preColor()
		drawComponent()
		
		// Enable lighting if enabled
		if ((!lighting()) && (Renderer().lighting())) {
			glEnable(GL_LIGHTING)
		}
	}
	
	override def shouldRender = {
		if (!isVisible) {
			false
		} else {
			super.shouldRender
		}
	}
	
	def isVisible = {
		checkVisible(this)
	}
	
	private def checkVisible(node: Node): Boolean = {
		node match {
			case null => true
			case component: Component => {
				if (component.visible()) {
					checkVisible(component.parent)
				} else {
					false
				}
			}
			case _ => checkVisible(node.parent)
		}
	}
	
	override protected def updateLocalMatrix(): Unit = {
		val s = Vec3d(scale.x(), scale.y(), scale.z())
		val r = Vec3d(rotation.actual.x(), rotation.actual.y(), rotation.actual.z())
		val t = Vec3d(location.actual.x(), location.actual.y(), location.actual.z())
		localMatrix() := transformation(s, MathUtil.eulerMat(r.x, r.y, r.z), t)
		
		localMatrix.changedLocal()
	}
	
	protected def initComponent() = {
	}
	
	protected def preRender() = {
	}
	
	private def preColor() = {
		val wc = worldColor()
		glColor4d(wc.red, wc.green, wc.blue, wc.alpha)
	}
	
	def drawComponent()
	
	private def updateActualLocation(evt: Event) = {
		var x = location.x()
		var y = location.y()
		var z = location.z()
		
		if (location.x.align() != HorizontalAlignment.Center) {
			if (location.x.align() == HorizontalAlignment.Left) {
				x += bounding().width / 2.0
			} else {
				x -= bounding().width / 2.0
			}
		}
		if (location.y.align() != VerticalAlignment.Middle) {
			if (location.y.align() == VerticalAlignment.Top) {
				y -= bounding().height / 2.0
			} else {
				y += bounding().height / 2.0
			}
		}
		if (location.z.align() != DepthAlignment.Middle) {
			if (location.z.align() == DepthAlignment.Front) {
				z += bounding().depth / 2.0
			} else {
				z -= bounding().depth / 2.0
			}
		}
		
		location.actual.set(x, y, z)
	}
	
	private def updateActualRotation(evt: Event) = {
		var x = rotation.x()
		var y = rotation.y()
		var z = rotation.z()
		
		if (rotation.x.align() != HorizontalAlignment.Center) {
			if (rotation.x.align() == HorizontalAlignment.Left) {
				x -= bounding().width / 2.0
			} else {
				x += bounding().width / 2.0
			}
		}
		if (rotation.y.align() != VerticalAlignment.Middle) {
			if (rotation.y.align() == VerticalAlignment.Top) {
				y += bounding().height / 2.0
			} else {
				y -= bounding().height / 2.0
			}
		}
		if (rotation.z.align() != DepthAlignment.Middle) {
			if (rotation.z.align() == DepthAlignment.Front) {
				z -= bounding().depth / 2.0
			} else {
				z += bounding().depth / 2.0
			}
		}
		rotation.actual.set(x, y, z)
	}
}
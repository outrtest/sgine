package org.sgine.ui

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.event.Listenable

import org.sgine.scene.ext.ColorNode
import org.sgine.scene.ext.MatrixNode

import org.sgine.property.AdvancedProperty
import org.sgine.property.DelegateProperty
import org.sgine.property.ImmutableProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.state.Stateful

import org.sgine.render.Renderable
import org.sgine.render.Renderer
import org.sgine.render.RenderUpdatable

import org.sgine.scene.NodeContainer

import org.sgine.ui.ext._

import simplex3d.math._
import simplex3d.math.doublem._

trait Component extends PropertyContainer with Renderable with RenderUpdatable with MatrixNode with ColorNode with Stateful {
	val id = new AdvancedProperty[String](null, this)
	val visible = new AdvancedProperty[Boolean](true, this)
	val renderer = new DelegateProperty(() => _renderer)
	
	private var firstRender = true
	
	private var _renderer: Renderer = _
	
	def update(renderer: Renderer) = {
	}
	
	def render(renderer: Renderer) = {
		_renderer = renderer
		if (visible()) {				// Only render if the component is visible
			if (firstRender) {
				firstRender = false
				initComponent()
			}
			
			preRender()
			Renderer.loadMatrix(worldMatrix())
			
			preColor()
			drawComponent()
		}
	}
	
	override protected def updateLocalMatrix(): Unit = {
		localMatrix() := Mat3x4d.Identity
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
	
	protected def drawComponent()
}
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
import simplex3d.math.doublem.DoubleMath._

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
	
	private val pscale = Vec3d(1)
	private val protation = Vec3d(0)
	private val ptranslation = Vec3d(0)
	
	override protected def updateLocalMatrix(): Unit = {
//		localMatrix() := Mat3x4d.Identity
//		localMatrix.changedLocal()
		
		// another hack
		if (this.isInstanceOf[ScaleComponent]) {
			val t = this.asInstanceOf[ScaleComponent]
			pscale.x = t.scale.x()
			pscale.y = t.scale.y()
			pscale.z = t.scale.z()
		}
		if (this.isInstanceOf[RotationComponent]) {
			val t = this.asInstanceOf[RotationComponent]
			protation.x = t.rotation.x()
			protation.y = t.rotation.y()
			protation.z = t.rotation.z()
		}
		if (this.isInstanceOf[LocationComponent]) {
			val t = this.asInstanceOf[LocationComponent]
			ptranslation.x = t.location.x()
			ptranslation.y = t.location.y()
			ptranslation.z = t.location.z()
		}
		
		import org.sgine.math.MathUtil._
		localMatrix() := transformation(pscale, eulerMat(protation.x, protation.y, protation.z), ptranslation)
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
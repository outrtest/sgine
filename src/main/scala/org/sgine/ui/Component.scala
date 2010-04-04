package org.sgine.ui

import org.lwjgl.opengl.GL11._

import org.sgine.event.Listenable

import org.sgine.math.mutable.Matrix4

import org.sgine.property.AdvancedProperty
import org.sgine.property.ImmutableProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.render.Renderable

import org.sgine.scene.NodeContainer

import org.sgine.ui.ext._

trait Component extends PropertyContainer with Renderable {
	val id = new AdvancedProperty[String](null, this)
	val visible = new AdvancedProperty[Boolean](true, this)
	
	val matrix = new ImmutableProperty[Matrix4](Matrix4().identity())
	
	def render() = {
		if (visible()) {				// Only render if the component is visible
			glLoadMatrix(matrix().buffer)
			
			drawComponent()
		}
	}
	
	def drawComponent()
}
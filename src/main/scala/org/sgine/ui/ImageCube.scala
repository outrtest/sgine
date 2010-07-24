package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.property.ImmutableProperty

import org.sgine.render.RenderImage
import org.sgine.render.TexturedQuad
import org.sgine.render.TextureManager

import org.sgine.ui.ext.AdvancedComponent

import scala.math._

class ImageCube extends CompositeComponent with AdvancedComponent {
	val front = new ImmutableProperty[Image](createComponent())
	val back = new ImmutableProperty[Image](createComponent())
	val top = new ImmutableProperty[Image](createComponent())
	val bottom = new ImmutableProperty[Image](createComponent())
	val left = new ImmutableProperty[Image](createComponent())
	val right = new ImmutableProperty[Image](createComponent())
	
	back().rotation.y := Pi
	top().rotation.x := Pi / -2.0
	bottom().rotation.x := Pi / 2.0
	left().rotation.y := Pi / -2.0
	right().rotation.y := Pi / 2.0
	
	this += front()
	this += back()
	this += top()
	this += bottom()
	this += left()
	this += right()
	
	def apply(resource: Resource, width: Double, height: Double): Unit = {
		val t = TextureManager(resource)
		val quad = new TexturedQuad()
		quad.texture := t
		quad.width := width
		quad.height := height
		
		apply(quad)
	}
	
	def apply(quad: TexturedQuad): Unit = {
		front().quad := quad
		back().quad := quad
		top().quad := quad
		bottom().quad := quad
		left().quad := quad
		right().quad := quad
		
		front().location.z := quad.width() / 2.0
		back().location.z := quad.width() / -2.0
		top().location.y := quad.height() / 2.0
		bottom().location.y := quad.height() / -2.0
		left().location.x := quad.width() / -2.0
		right().location.x := quad.width() / 2.0
	}
	
	protected def createComponent() = new Image()
}
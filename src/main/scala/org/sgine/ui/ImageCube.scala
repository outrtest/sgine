package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.RenderImage
import org.sgine.render.TexturedQuad
import org.sgine.render.TextureManager

class ImageCube extends Cube[Image] {
//	def apply(resource: Resource, width: Double, height: Double) = {
//		val t = TextureManager(resource)
//		val renderImage = RenderImage(t)
//		
//		front().renderImage := renderImage
//		back().renderImage := renderImage
//		top().renderImage := renderImage
//		bottom().renderImage := renderImage
//		left().renderImage := renderImage
//		right().renderImage := renderImage
//		
//		front().location.z := width / 2.0
//		back().location.z := width / -2.0
//		top().location.y := height / 2.0
//		bottom().location.y := height / -2.0
//		left().location.x := width / -2.0
//		right().location.x := width / 2.0
//	}
//	
//	protected def createComponent() = new Image()
	
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
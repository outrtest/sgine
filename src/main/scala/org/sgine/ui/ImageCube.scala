package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.{Image => RenderImage}
import org.sgine.render.TextureUtil

class ImageCube extends Cube[Image] {
	def apply(resource: Resource, width: Double, height: Double) = {
		val t = TextureUtil(resource.url)
		val renderImage = RenderImage(t)
		
		front().renderImage := renderImage
		back().renderImage := renderImage
		top().renderImage := renderImage
		bottom().renderImage := renderImage
		left().renderImage := renderImage
		right().renderImage := renderImage
		
		front().location.z := width / 2.0
		back().location.z := width / -2.0
		top().location.y := height / 2.0
		bottom().location.y := height / -2.0
		left().location.x := width / -2.0
		right().location.x := width / 2.0
	}
	
	protected def createComponent() = new Image()
}
package org.sgine.render.awt

import org.sgine.render.Image
import org.sgine.render.TextureUtil

import org.sgine.util.GeneralReusableGraphic

import scala.math._

class ShapeRenderableTexture(val shape: java.awt.Shape) extends ShapeRenderable {
	private var xOffset: Double = 0.0
	private var yOffset: Double = 0.0
	private var image: Image = _
	
	def apply() = {
		if (image == null) {
			val bounds = shape.getBounds
			xOffset = bounds.getX
			yOffset = bounds.getY
			val width = round(bounds.getWidth).toInt
			val height = round(bounds.getHeight).toInt
			val g = GeneralReusableGraphic(width, height)
			try {
				g.translate(-xOffset, -yOffset)
				g.setColor(java.awt.Color.BLUE)
				g.draw(shape)
				
				val texture = TextureUtil(GeneralReusableGraphic(), width, height)
				image = Image(texture)
			} finally {
				GeneralReusableGraphic.release()
			}
		}
		
		image.draw(xOffset, yOffset)
	}
}
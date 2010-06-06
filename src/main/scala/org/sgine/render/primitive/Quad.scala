package org.sgine.render.primitive

import org.sgine.core.Color

import org.sgine.render.Image

import org.lwjgl.opengl.GL11._

class Quad protected(val width: Double, val height: Double, val color: Color, val image: Image) extends Primitive {
	val mode = GL_QUADS
	
	val vertexCount = 4
	
	def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)
	
	def texture(index: Int) = {
		if (image != null) {
			val x1 = image.x / image.texture.width
			val y1 = image.y / image.texture.height
			val x2 = (image.x + image.width) / image.texture.width
			val y2 = (image.y + image.height) / image.texture.height
			index match {
				case 0 => glTexCoord2d(x1, y2)
				case 1 => glTexCoord2d(x2, y2)
				case 2 => glTexCoord2d(x2, y1)
				case 3 => glTexCoord2d(x1, y1)
			}
		}
	}
	
	def vertex(index: Int) = index match {
		case 0 => glVertex3d(-width / 2.0, -height / 2.0, 0.0)
		case 1 => glVertex3d(width / 2.0, -height / 2.0, 0.0)
		case 2 => glVertex3d(width / 2.0, height / 2.0, 0.0)
		case 3 => glVertex3d(-width / 2.0, height / 2.0, 0.0)
	}
	
	override def begin() = {
		if (image != null) {
			image.texture.bind()
		}
		super.begin()
	}
}

object Quad {
	def apply(width: Double, height: Double, color: Color = Color.White, image: Image = null) = new Quad(width, height, color, image)
}
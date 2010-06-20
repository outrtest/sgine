package org.sgine.render

import org.lwjgl.opengl.GL11._

class RenderImage protected() extends Function0[Unit] {
	var texture: Texture = _
	var x: Double = _
	var y: Double = _
	var width: Double = _
	var height: Double = _
	
	def apply() = draw()
	
	def draw(offsetX: Double = 0.0, offsetY: Double = 0.0, insetX1: Double = 0.0, insetY1: Double = 0.0, insetX2: Double = 0.0, insetY2: Double = 0.0) = {
		if (texture != null) {
			texture.bind()

			val x1 = (x + insetX1) / texture.width
			val y1 = (y + insetY1) / texture.height
			val x2 = (x + width - insetX2) / texture.width
			val y2 = (y + height - insetY2) / texture.height
			val w = (width - insetX1 - insetX2) / 2.0
			val h = (height - insetY1 - insetY2) / 2.0
			
//			println("DRAW:" + x1 + "x" + y1 + " - " + x2 + "x" + y2 + " - " + w + "x" + h)
			
			glBegin(GL_QUADS)
			
			// Bottom-Left
			glTexCoord2d(x1, y2)
			glVertex3d(-w + offsetX, -h + offsetY, 0.0)
			
			// Bottom-Right
			glTexCoord2d(x2, y2)
			glVertex3d(w + offsetX, -h + offsetY, 0.0)
			
			// Top-Right
			glTexCoord2d(x2, y1)
			glVertex3d(w + offsetX, h + offsetY, 0.0)
			
			// Top-Left
			glTexCoord2d(x1, y1)
			glVertex3d(-w + offsetX, h + offsetY, 0.0)
			
			glEnd()
			
			texture.unbind()
		}
	}
}

object RenderImage {
	def apply() = new RenderImage()
	
	def apply(texture: Texture) = {
		val i = new RenderImage()
		i.texture = texture
		i.width = texture.width
		i.height = texture.height
		
		i
	}
	
	def apply(texture: Texture, x: Double, y: Double, width: Double, height: Double) = {
		val i = new RenderImage()
		i.texture = texture
		i.x = x
		i.y = y
		i.width = width
		i.height = height
		
		i
	}
}
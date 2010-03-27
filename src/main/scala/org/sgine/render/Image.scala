package org.sgine.render

import org.lwjgl.opengl.GL11._

class Image {
	var texture: Texture = _
	var x: Float = _
	var y: Float = _
	var width: Float = _
	var height: Float = _
	
	def draw(offsetX: Float = 0.0f, offsetY: Float = 0.0f, insetX1: Float = 0.0f, insetY1: Float = 0.0f, insetX2: Float = 0.0f, insetY2: Float = 0.0f) = {
		if (texture != null) {
			texture.bind()
		}
		
		val x1 = (x + insetX1) / texture.width
		val y1 = (y + insetY1) / texture.height
		val x2 = (x + width - insetX2) / texture.width
		val y2 = (y + height - insetY2) / texture.height
		val w = (width - insetX1 - insetX2) / 2.0f
		val h = (height - insetY1 - insetY2) / 2.0f
		
		glBegin(GL_QUADS)
		
		// Bottom-Left
		glTexCoord2f(x1, y2)
		glVertex3f(-w + offsetX, -h + offsetY, 0.0f)
		
		// Bottom-Right
		glTexCoord2f(x2, y2)
		glVertex3f(w + offsetX, -h + offsetY, 0.0f)
		
		// Top-Right
		glTexCoord2f(x2, y1)
		glVertex3f(w + offsetX, h + offsetY, 0.0f)
		
		// Top-Left
		glTexCoord2f(x1, y1)
		glVertex3f(-w + offsetX, h + offsetY, 0.0f)
		
		glEnd()
	}
}
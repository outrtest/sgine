package org.sgine.render

import org.lwjgl.opengl.GL11._

class Image {
	var texture: Texture = _
	var x: Float = _
	var y: Float = _
	var width: Float = _
	var height: Float = _
	
	def draw() = {
		if (texture != null) {
			texture.bind()
		}
		
		val x1 = x / texture.width
		val y1 = y / texture.height
		val x2 = (x + width) / texture.width
		val y2 = (y + height) / texture.height
		val w = width / 2.0f
		val h = height / 2.0f
		
		glBegin(GL_QUADS)
		
		// Bottom-Left
		glTexCoord2f(x1, y2)
		glVertex3f(-w, -h, 0.0f)
		
		// Bottom-Right
		glTexCoord2f(x2, y2)
		glVertex3f(w, -h, 0.0f)
		
		// Top-Right
		glTexCoord2f(x2, y1)
		glVertex3f(w, h, 0.0f)
		
		// Top-Left
		glTexCoord2f(x1, y1)
		glVertex3f(-w, h, 0.0f)
		
		glEnd()
	}
}
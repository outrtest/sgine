package org.sgine.ui.ext

import org.lwjgl.opengl.GL11._

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.render.Texture

import org.sgine.ui.Text

class Caret(override val parent: Text) extends PropertyContainer {
	val enabled = new AdvancedProperty[Boolean](false, this)
	val position = new AdvancedProperty[Int](0, this)
	val width = new AdvancedProperty[Double](2.0, this)
	
	def draw() = {
		// TODO: handle better
		if (parent.lines.length > 0) {
			Texture.unbind()
			
			val line = parent.lines(0)
			val c = line.characters(0)
			val x = c.x - (c.char.xAdvance / 2.0) - 2
			val y = c.y// - line.font.lineHeight
			val width = this.width()
			glBegin(GL_QUADS)
			glVertex3d(x, y - 50.0, 0.0)
			glVertex3d(x + width, y - 50.0, 0.0)
			glVertex3d(x + width, y, 0.0)
			glVertex3d(x, y, 0.0)
			glEnd()
		}
	}
}
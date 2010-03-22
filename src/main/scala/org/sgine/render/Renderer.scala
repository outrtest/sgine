package org.sgine.render

import org.sgine.core.Color
import org.sgine.core.mutable.{Color => MutableColor}
import org.sgine.math.mutable.{Matrix4 => MutableMatrix4}

import org.lwjgl.opengl.GL11._

/**
 * Renderer provides an abstract context to gain access to the rendering
 * implementation in order to provide a simple and swappable rendering
 * abstraction.
 * 
 * @author Matt Hicks
 */
class Renderer {
	val matrix = MutableMatrix4().identity()
	
	private val backgroundColor = new MutableColor()
	private val foregroundColor = new MutableColor()
	private val buffer = matrix.getTranspose(null, false)
	
	var lineWidth = 1.0f
	
	def background_=(color: Color): Unit = background(color.red, color.green, color.blue, color.alpha)
	
	def background(red: Float = 0.0f, green: Float = 0.0f, blue: Float = 0.0f, alpha: Float = 1.0f): Unit = {
		backgroundColor.red = red
		backgroundColor.green = green
		backgroundColor.blue = blue
		backgroundColor.alpha = alpha
		
		glClearColor(red, green, blue, alpha)
	}
	
	def background: Color = backgroundColor
	
	def clear() = glClear(GL_COLOR_BUFFER_BIT)
	
	def color_=(c: Color): Unit = color(c.red, c.green, c.blue, c.alpha)
	
	def color(red: Float = 0.0f, green: Float = 0.0f, blue: Float = 0.0f, alpha: Float = 1.0f): Unit = {
		foregroundColor.red = red
		foregroundColor.green = green
		foregroundColor.blue = blue
		foregroundColor.alpha = alpha
		
		glColor4f(red, green, blue, alpha)
	}
	
	def color: Color = foregroundColor
	
	def drawRect(x: Float, y: Float, width: Float, height: Float) = {
		drawLine(x, y, x + width, y)
		drawLine(x + width, y, x + width, y + height)
		drawLine(x + width, y + height, x, y + height)
		drawLine(x, y + height, x, y)
	}
	
	def drawLine(x1: Float, y1: Float, x2: Float, y2: Float) = {
		val lineWidth = this.lineWidth - 1.0f
		if (x1 == x2) {
			var _y1 = y1
			var _y2 = y2
			if (y1 > y2) {
				_y2 = y1
				_y1 = y2
			}
			fillRect(x1 - (lineWidth / 2.0f), _y1 - (lineWidth / 2.0f), lineWidth + 1.0f, (_y2 - _y1) + lineWidth + 1.0f)
		} else if (y1 == y2) {
			var _x1 = x1
			var _x2 = x2
			if (x1 > x2) {
				_x2 = x1
				_x1 = x2
			}
			fillRect(_x1 - (lineWidth / 2.0f), y1 - (lineWidth / 2.0f), (_x2 - _x1) + lineWidth + 1.0f, lineWidth + 1.0f)
		} else {
			// TODO: draw vertex
			throw new RuntimeException("Unimplemented feature!")
		}
	}
	
	def fillRect(x: Float, y: Float, width: Float, height: Float) = {
		glDisable(GL_TEXTURE_2D);
		
		glBegin(GL_QUADS)
		glVertex2f(x, y)
		glVertex2f(x + width, y)
		glVertex2f(x + width, y + height)
		glVertex2f(x, y + height)
		glEnd()
	}
	
	def drawImage(image: Image, x: Float, y: Float) = {
//		image.draw(x, y, y)
	}
	
	def transform() = {
		matrix.getTranspose(buffer, false)
		glLoadMatrix(buffer)
	}
}
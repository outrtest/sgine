package org.sgine.render

import org.lwjgl.opengl.GL11._

import scala.math._

/**
 * Graphics3D is inspired and a partial port of Slick2D's org.newdawn.slick.Graphics class.
 * 
 * The purpose of this object is to draw directly in OpenGL standard graphics functionality.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Graphics3D {
	var lineWidth = 1.0
	
	def drawArc(x: Double, y: Double, width: Double, height: Double, segments: Int, start: Double, end: Double) = {
		val s = start
		var e = end
		while (e < s) {
			e += 360.0
		}
		
		val cx = x + (width / 2.0)
		val cy = y + (height / 2.0)
		
		glBegin(GL_LINE_STRIP)
		
		val step = 360.0 / segments
		var a = s
		while (a < (e + s).toInt) {
			var ang = a
			if (ang > e) {
				ang = e
			}
			val x = cx + cos(ang.toRadians) * width / 2.0
			val y = cy + sin(ang.toRadians) * height / 2.0
			glVertex2d(x, y)
			
			a += step
		}
		
		glEnd()
	}
	
	def drawLine(x1: Double, y1: Double, x2: Double, y2: Double) = {
		val lineWidth = this.lineWidth
		glLineWidth(lineWidth.toFloat)
		
		glBegin(GL_LINE_STRIP)
		glVertex2d(x1, y1)
		glVertex2d(x2, y2)
		glEnd()
	}
	
	def drawRect(x: Double, y: Double, width: Double, height: Double) = {
		val lineWidth = this.lineWidth
		
		drawLine(x, y, x + width, y)
		drawLine(x + width, y, x + width, y + height)
		drawLine(x + width, y + height, x, y + height)
		drawLine(x, y + height, x, y)
	}
	
	def drawRoundRect(x: Double, y: Double, width: Double, height: Double, cornerRadius: Int, segments: Int = 50) = {
		if (cornerRadius < 0) throw new IllegalArgumentException("corner radius must be > 0")
		if (cornerRadius == 0) {
			drawRect(x, y, width, height)
		} else {
			val mr = round(min(width, height) / 2.0).toInt
			// Make sure that w & h are larger than 2 * cornerRadius
			val cr = if (cornerRadius > mr) {
				mr
			} else {
				cornerRadius
			}
			
			drawLine(x + cr, y, x + width - cr, y)
			drawLine(x, y + cr, x, y + height - cr)
			drawLine(x + width, y + cr, x + width, y + height - cr)
			drawLine(x + cr, y + height, x + width - cr, y + height)
			
			val d = cr * 2.0
			// bottom right - 0, 90
			drawArc(x + width - d, y + height - d, d, d, segments, 0, 90)
			// bottom left - 90, 180
			drawArc(x, y + height - d, d, d, segments, 90, 180)
			// top right - 270, 360
			drawArc(x + width - d, y, d, d, segments, 270, 360)
			// top left - 180, 270
			drawArc(x, y, d, d, segments, 180, 270)
		}
	}
}
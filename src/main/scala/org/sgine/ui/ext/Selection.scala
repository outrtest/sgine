package org.sgine.ui.ext

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.render.Texture
import org.sgine.render.font.RenderedCharacter
import org.sgine.render.font.RenderedLine

import org.sgine.ui.Text

import scala.math._

class Selection(override val parent: Text) extends PropertyContainer {
	val visible = new AdvancedProperty[Boolean](true, this)
	val begin = new AdvancedProperty[Int](0, this, filter = filterSelection)
	val end = new AdvancedProperty[Int](0, this, filter = filterSelection)
	val color = new AdvancedProperty[Color](Color.SelectBlue.set(alpha = 0.5), this)
	val mouseEnabled = new AdvancedProperty[Boolean](true, this)
	val keyboardEnabled = new AdvancedProperty[Boolean](true, this)
	
	private var l = 0
	private var r = 0
	private var c1: RenderedCharacter = null
	private var c2: RenderedCharacter = null
	
	def left = min(begin(), end())
	def right = max(begin(), end())
	
	def all() = {
		begin := 0
		end := Int.MaxValue
	}
	
	def apply(begin: Int, end: Int) = {
		this.begin := begin
		this.end := end
	}
	
	def draw() = {
		if (visible()) {
			if (begin() != end()) {
				if ((left != l) || (right != r)) {
					// Revalidate bounds
					l = left
					r = right
					
					c1 = parent.char(l).getOrElse(null)
					c2 = parent.char(r).getOrElse(null)
					
					// Reset blink for editable caret
					parent.caret.elapsed = 0.0
					parent.caret.toggle = true
				}
				
				if ((l != r) && (c1 != null) && (c2 != null)) {
					Texture.unbind()
					
					if (c1.line == c2.line) {
						drawLine(c1, c2)
					} else {
						for (line <- getLines()) {
							if (line == c1.line) {
								drawLine(c1, line.characters.last)
							} else if (line == c2.line) {
								drawLine(line.characters.head, c2)
							} else {
								drawLine(line.characters.head, line.characters.last)
							}
						}
					}
					
					if (parent.editable()) {
						parent.caret.drawCaret(parent.char(end()).getOrElse(null))
					}
				}
			}
		}
	}
	
	private def getLines() = {
		var lines: List[RenderedLine] = Nil
		
		var foundFirst = false
		var foundSecond = false
		for (l <- parent.lines) {
			if (foundSecond) {
				// Ignore the rest
			} else if (c1.line == l) {
				foundFirst = true
				lines = l :: lines
			} else if (c2.line == l) {
				foundSecond = true
				lines = l :: lines
			} else if (foundFirst) {
				lines = l :: lines
			}
		}
		
		lines.reverse
	}
	
	private def drawLine(c1: RenderedCharacter, c2: RenderedCharacter) = {
		val x1 = c1.x - (c1.char.xAdvance / 2.0) - 2.0
		val y1 = c1.y - (c1.line.font.lineHeight / 2.0)
		val x2 = c2.x - (c2.char.xAdvance / 2.0)
		val y2 = c1.y + (c1.line.font.lineHeight / 2.0)
		val color = this.color()
		
		glColor4d(color.red, color.green, color.blue, color.alpha)
		glBegin(GL_QUADS)
		glVertex3d(x1, y1, 0.0)
		glVertex3d(x2, y1, 0.0)
		glVertex3d(x2, y2, 0.0)
		glVertex3d(x1, y2, 0.0)
		glEnd()
	}
	
	// Make sure the position isn't set to something unreasonable
	private def filterSelection(p: Int) = {
		if (p >= parent.characters().length) {
			parent.characters().length - 1
		} else if (p < 0) {
			0
		} else {
			p
		}
	}
}
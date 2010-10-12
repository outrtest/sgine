package org.sgine.ui.ext

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

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
	def text = {
		val l = left
		val r = right
		if (r - l > 0) {
			val b = new StringBuilder()
			for (index <- left until right) {
				b.append(parent.char(index).get.char)
			}
			b.toString
		} else {
			""
		}
	}
	def length = right - left
	
	private var l = 0
	private var r = 0
	private var c1: RenderedCharacter = null
	private var c2: RenderedCharacter = null
	private var lines: List[SelectedLine] = Nil
	
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
	
	def backspace() = {
		val p = parent.caret.position()
		if (p != -1) {
			if (p > 0) {
				val changed = parent.textWithout(p - 1, p - 1)
				parent.text := changed
				parent.caret.position(p - 1, false)
				parent.caret.position.changed(false)
			}
		} else {
			val l = left
			val changed = parent.textWithout(left, right - 1)
			parent.text := changed
			parent.caret.position(l, false)
			parent.caret.position.changed(false)
		}
	}
	
	def delete() = {
		val p = parent.caret.position()
		if (p != -1) {
			val changed = parent.textWithout(p, p)
			parent.text := changed
			parent.caret.position(p, false)
			parent.caret.position.changed(false)
		} else {
			val l = left
			val changed = parent.textWithout(left, right - 1)
			parent.text := changed
			parent.caret.position(l, false)
			parent.caret.position.changed(false)
		}
	}
	
	def cut() = {
		if (length > 0) {
			copy()
			delete()
		}
	}
	
	def copy() = {
		val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
		val stringSelection = new StringSelection(text)
		clipboard.setContents(stringSelection, stringSelection)
	}
	
	def paste() = {
		val clipboard = Toolkit.getDefaultToolkit.getSystemClipboard
		val transferable = clipboard.getContents(null)
		if (transferable != null) {
			if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				val s = transferable.getTransferData(DataFlavor.stringFlavor).toString
				insert(s)
			}
		}
	}
	
	def insert(value: String) = {
		val p = parent.caret.position()
		if (p != -1) {
			val changed = parent.textInsert(p, value)
			parent.text := changed
			parent.caret.position(p + value.length, false)
			parent.caret.position.changed(false)
		} else {
			val l = left
			val changed = parent.textWithout(left, right - 1, value)
			parent.text := changed
			parent.caret.position(l + value.length, false)
			parent.caret.position.changed(false)
		}
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
					val start = parent.lines.indexOf(c1.line)
					val end = c2 match {
						case null => parent.lines.length - 1
						case c => parent.lines.indexOf(c.line)
					}
					
					lines = generateLines(start, end, Nil).reverse
					
					// Reset blink for editable caret
					parent.caret.elapsed = 0.0
					parent.caret.toggle = true
					
					// Update caret position
					parent.caret.positionChanged(null)
					
					// Update System Selection
					val selectionClipboard = Toolkit.getDefaultToolkit.getSystemSelection
					if (selectionClipboard != null) {
						val stringSelection = new StringSelection(text)
						selectionClipboard.setContents(stringSelection, stringSelection)
					}
				}
				
				if ((l != r) && (lines != Nil)) {
					Texture.unbind()
					
					lines.foreach(drawLine)
				}
			}
		}
	}
	
	private def generateLines(current: Int, end: Int, lines: List[SelectedLine]): List[SelectedLine] = {
		val line = parent.lines(current)
		val startChar = c1 match {
			case null => line.characters.head
			case c if (line == c.line) => c
			case c => line.characters.head
		}
		var eol = false
		val endChar = c2 match {
			case null => {
				if (current == end) {
					eol = true
				}
				line.characters.last
			}
			case c if (line == c.line) => c
			case c => line.characters.last
		}
		val x1 = startChar.x - (startChar.fontChar.xAdvance / 2.0) - 2.0
		val y1 = startChar.y - (startChar.line.font.lineHeight / 2.0)
		val x2 = eol match {
			case true => endChar.x + (endChar.fontChar.xAdvance / 2.0)
			case false => endChar.x - (endChar.fontChar.xAdvance / 2.0)
		}
		val y2 = startChar.y + (startChar.line.font.lineHeight / 2.0)
		val sl = new SelectedLine(x1, y1, x2, y2)
		if (current == end) {
			sl :: lines
		} else {
			generateLines(current + 1, end, sl :: lines)
		}
	}
	
	private val drawLine = (sl: SelectedLine) => {
		val color = this.color()
		
		glColor4d(color.red, color.green, color.blue, color.alpha)
		glBegin(GL_QUADS)
		glVertex3d(sl.x1, sl.y1, 0.0)
		glVertex3d(sl.x2, sl.y1, 0.0)
		glVertex3d(sl.x2, sl.y2, 0.0)
		glVertex3d(sl.x1, sl.y2, 0.0)
		glEnd()
	}
	
	// Make sure the position isn't set to something unreasonable
	private def filterSelection(p: Int) = {
		if (p > parent.characters().length) {
			parent.characters().length
		} else if (p < 0) {
			0
		} else {
			p
		}
	}
}

class SelectedLine(val x1: Double, val y1: Double, val x2: Double, val y2: Double)
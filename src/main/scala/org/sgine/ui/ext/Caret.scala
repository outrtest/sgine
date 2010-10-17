package org.sgine.ui.ext

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Renderer
import org.sgine.render.Texture
import org.sgine.render.font.RenderedCharacter

import org.sgine.scene.ext.FocusableNode

import org.sgine.ui.Text

import scala.math._

class Caret(override val parent: Text) extends PropertyContainer {
	val visible = new AdvancedProperty[Boolean](false, this, dependency = parent.style.caret.visible)
	val position = new AdvancedProperty[Int](0, this, filter = filterPosition)
	val width = new AdvancedProperty[Double](0.0, this, dependency = parent.style.caret.width)
	val color = new AdvancedProperty[Color](null, this, dependency = parent.style.caret.color)
	val rate = new NumericProperty(0.0, this, null, parent.style.caret.rate)
	val mouseEnabled = new AdvancedProperty[Boolean](false, this, dependency = parent.style.caret.mouseEnabled)
	val keyboardEnabled = new AdvancedProperty[Boolean](false, this, dependency = parent.style.caret.keyboardEnabled)
	
	protected[ui] var caretDisplay: Boolean = _
	protected[ui] var caretX: Double = _
	protected[ui] var caretY: Double = _
	protected[ui] var caretWidth: Double = _
	protected[ui] var caretHeight: Double = _
	
	protected[ui] var elapsed = 0.0
	protected[ui] var toggle = true
	
	Listenable.listenTo(EventHandler(updatePosition, ProcessingMode.Blocking), position, parent.selection.begin, parent.selection.end)
	private val positionChangeType = new java.util.concurrent.atomic.AtomicInteger(0)		// 0 = no change, 1 = position, 2 = selection
	
	position.listeners += EventHandler(updatePosition, ProcessingMode.Blocking)
	parent.lines.listeners += EventHandler(updatePosition, ProcessingMode.Blocking)
	
	def draw() = {
		// Blink
		if (parent.editable()) {
			elapsed += Renderer().time
			if (elapsed >= rate()) {
				elapsed = 0.0
				toggle = !toggle
			}
			toggle
		} else {
			toggle = true
		}
		
		// Update selection and position
		positionChangeType.get match {
			case 1 if (position() != -1) => {
				parent.selection.begin := position()
				parent.selection.end := position()
				positionChanged(null)
			}
			case 2 => if (parent.selection.begin() == parent.selection.end()) {
				position := parent.selection.begin()
				positionChanged(null)
			} else {
				position := -1
				positionChanged(null)
			}
			case _ =>
		}
		positionChangeType.set(0)
		
		if ((shouldBeVisible) && (caretDisplay)) {
			drawCaret()
		}
	}
	
	protected[ui] def drawCaret() = {
		if (toggle) {
			Texture.unbind()
			
			val x = caretX
			val y = caretY
			val width = caretWidth
			val height = caretHeight
			val color = this.color()
			
			var x1 = x - width
			var y1 = y - height
			var x2 = x + width
			var y2 = y + height
			
			if (parent.clip.enabled()) {
				x1 = max(x1, parent.clip.x1())
				y1 = max(y1, parent.clip.y1())
				x2 = min(x2, parent.clip.x2())
				y2 = min(y2, parent.clip.y2())
			}
			
			if ((x1 < x2) && (y1 < y2)) {
				// TODO: handle better
				glColor4d(color.red, color.green, color.blue, color.alpha)
				glBegin(GL_QUADS)
				glVertex3d(x1, y1, 0.1)
				glVertex3d(x2, y1, 0.1)
				glVertex3d(x2, y2, 0.1)
				glVertex3d(x1, y2, 0.1)
				glEnd()
			}
		}
	}
	
	private def shouldBeVisible() = {
		visible() match {
			case true if (FocusableNode.focused() == parent) => true
			case _ => false
		}
	}
	
	private def updatePosition(evt: PropertyChangeEvent[_]) = {
		if (evt.listenable == position) {
			positionChangeType.set(1)
		} else {
			positionChangeType.set(2)
		}
	}
	
	protected[ui] def positionChanged(evt: PropertyChangeEvent[_]) = {
		val position = this.position() match {
			case -1 if (parent.editable()) => parent.selection.end()
			case p => p
		}
		position match {
			case -1 => caretDisplay = false							// Selection
			case p if (parent.characters().length == 0) => {		// No text
				caretDisplay = true
				caretX = 0.0
				caretY = 0.0
				caretWidth = this.width() / 2.0
				caretHeight = parent.font().lineHeight / 2.5
			}
			case p if (p == parent.characters().length) => {		// End of line
				val c = parent.characters().last
				if (c.char == '\n') {
					caretX = 0.0
					caretY = c.y - c.line.font.lineHeight
					caretWidth = this.width() / 2.0
					caretHeight = c.line.font.lineHeight / 2.5
				} else {
					caretX = c.x + (c.fontChar.xAdvance / 2.0)
					caretY = c.y
					caretWidth = this.width() / 2.0
					caretHeight = c.line.font.lineHeight / 2.5
				}
			}
			case p => parent.char(p) match {						// Normal character (show in front of character)
				case Some(c) => {
					caretDisplay = true
					updateCaret(c)
					
					// Reset blink so we see the movement
					elapsed = 0.0
					toggle = true
				}
				case None => caretDisplay = false
			}
		}
	}
	
	protected[ui] def updateCaret(c: RenderedCharacter) = {
		caretX = c.x - (c.fontChar.xAdvance / 2.0) + 0.5
		caretY = c.y
		caretWidth = this.width() / 2.0
		caretHeight = c.line.font.lineHeight / 2.5
	}
	
	// Make sure the position isn't set to something unreasonable
	private def filterPosition(p: Int) = {
		if (p == -1) {
			-1
		} else if (p > parent.characters().length) {
			parent.characters().length
		} else if (p < 0) {
			0
		} else {
			p
		}
	}
}
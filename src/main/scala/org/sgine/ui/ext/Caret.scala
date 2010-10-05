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

class Caret(override val parent: Text) extends PropertyContainer {
	val visible = new AdvancedProperty[Boolean](true, this)
	val position = new AdvancedProperty[Int](0, this, filter = filterPosition)
	val width = new AdvancedProperty[Double](1.0, this)
	val color = new AdvancedProperty[Color](Color.White, this)
	val rate = new NumericProperty(0.5, this)
	val mouseEnabled = new AdvancedProperty[Boolean](true, this)
	val keyboardEnabled = new AdvancedProperty[Boolean](true, this)
	
	private var character: RenderedCharacter = null
	protected[ui] var elapsed = 0.0
	protected[ui] var toggle = true
	
	Listenable.listenTo(EventHandler(updatePosition, ProcessingMode.Blocking), position, parent.selection.begin, parent.selection.end)
	private val positionChangeType = new java.util.concurrent.atomic.AtomicInteger(0)		// 0 = no change, 1 = position, 2 = selection
	
	position.listeners += EventHandler(positionChanged, ProcessingMode.Blocking)
	parent.lines.listeners += EventHandler(positionChanged, ProcessingMode.Blocking)
	
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
			}
			case 2 => if (parent.selection.begin() == parent.selection.end()) {
				position := parent.selection.begin()
			} else {
				position := -1
			}
			case _ =>
		}
		positionChangeType.set(0)
		
		if ((shouldBeVisible) && (character != null)) {
			drawCaret(character)
		}
	}
	
	protected[ui] def drawCaret(c: RenderedCharacter) = {
		if (toggle) {
			Texture.unbind()
				
			val line = c.line
			val x = c.x - (c.char.xAdvance / 2.0) - 2.0
			val y = c.y
			val h = line.font.lineHeight / 2.5
			val width = this.width()
			val color = this.color()
			
			// TODO: handle better
			glColor4d(color.red, color.green, color.blue, color.alpha)
			glBegin(GL_QUADS)
			glVertex3d(x, y - h, 0.0)
			glVertex3d(x + width, y - h, 0.0)
			glVertex3d(x + width, y + h, 0.0)
			glVertex3d(x, y + h, 0.0)
			glEnd()
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
	
	private def positionChanged(evt: PropertyChangeEvent[_]) = {
		position() match {
			case -1 => character = null
			case p => parent.char(p) match {
				case Some(c) => {
					character = c
					
					// Reset blink so we see the movement
					elapsed = 0.0
					toggle = true
				}
				case None =>
			}
		}
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
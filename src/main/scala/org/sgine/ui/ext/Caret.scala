package org.sgine.ui.ext

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.event.EventHandler

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.Renderer
import org.sgine.render.Texture
import org.sgine.render.font.RenderedCharacter

import org.sgine.ui.Text

class Caret(override val parent: Text) extends PropertyContainer {
	val visible = new AdvancedProperty[Boolean](true, this)
	val position = new AdvancedProperty[Int](0, this, filter = filterPosition)
	val width = new AdvancedProperty[Double](1.0, this)
	val color = new AdvancedProperty[Color](Color.White, this)
	val rate = new NumericProperty(0.5, this)
	
	private var character: RenderedCharacter = null
	private var elapsed = 0.0
	private var toggle = true
	
//	position.bind(parent.selection.begin, updatePosition)
//	position.bind(parent.selection.end, updatePosition)
//	
//	parent.selection.begin.bind(position, updateSelection)
//	parent.selection.end.bind(position, updateSelection)
	
	position.listeners += EventHandler(positionChanged, ProcessingMode.Blocking)
	parent.lines.listeners += EventHandler(positionChanged, ProcessingMode.Blocking)
	
	def draw() = {
		// TODO: handle better
		// TODO: only draw if currently focused
		// TODO: support blink if editable
		
		if ((shouldBeVisible) && (character != null)) {
			Texture.unbind()
			
			val c = character
			val line = c.line
			val x = c.x - (c.char.xAdvance / 2.0) - 2
			val y = c.y
			val h = line.font.lineHeight / 2.5
			val width = this.width()
			val color = this.color()
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
			case true => {
				if (parent.editable()) {
					elapsed += Renderer().time
					if (elapsed >= rate()) {
						elapsed = 0.0
						toggle = !toggle
					}
					toggle
				} else {
					true
				}
			}
			case false => false
		}
	}
	
	private def updatePosition(value: Int) = {
		if (parent.selection.begin() == parent.selection.end()) {
			if (value != position()) {
				Some(value)
			} else {
				None
			}
		} else {
			Some(-1)
		}
	}
	
	private def updateSelection(i: Int) = if (i != -1) Some(i) else None
	
	private def positionChanged(evt: PropertyChangeEvent[_]) = {
		position() match {
			case -1 =>
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
		if (p > parent.text().length) {
			parent.text().length
		} else if (p < 0) {
			0
		} else {
			p
		}
	}
}
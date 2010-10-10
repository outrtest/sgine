package org.sgine.ui

import org.sgine.core.HorizontalAlignment
import org.sgine.core.ProcessingMode
import org.sgine.core.VerticalAlignment

import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.input.Key
import org.sgine.input.event._

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.font.BitmapFont
import org.sgine.render.font.Font
import org.sgine.render.font.FontManager
import org.sgine.render.font.RenderedCharacter
import org.sgine.render.font.RenderedLine
import org.sgine.render.font.WordWrap

import org.sgine.scene.ext.FocusableNode

import org.sgine.ui.ext.Caret
import org.sgine.ui.ext.Selection

import org.lwjgl.opengl.GL11._

class Text extends ShapeComponent with FocusableNode {
	val cull = _cull
	val font = new AdvancedProperty[Font](FontManager("Arial32"), this)
	val text = new AdvancedProperty[String]("", this)
	val kern = new AdvancedProperty[Boolean](true, this)
	val horizontalAlignment = new AdvancedProperty[HorizontalAlignment](HorizontalAlignment.Center, this)
	val verticalAlignment = new AdvancedProperty[VerticalAlignment](VerticalAlignment.Middle, this)
	val editable = new AdvancedProperty[Boolean](false, this)
	
	protected[ui] val lines = new AdvancedProperty[Seq[RenderedLine]](Nil, this)
	protected[ui] val characters = new AdvancedProperty[Seq[RenderedCharacter]](Nil, this)
	protected[ui] def char(index: Int) = characters() match {
		case t if (t.length > index) => Some(t(index))
		case _ => None
	}
	protected[ui] def textWithout(start: Int, end: Int) = {
		val b = new StringBuilder()
		val characters = this.characters()
		for (index <- 0 until characters.length) {
			if ((index >= start) && (index <= end)) {
				// Ignore
			} else {
				b.append(characters(index).char)
			}
		}
		b.toString
	}
	protected[ui] def textInsert(position: Int, value: String) = {
		val b = new StringBuilder()
		val characters = this.characters()
		var inserted = false
		for (index <- 0 until characters.length) {
			if (index == position) {
				b.append(value)
				inserted = true
			}
			b.append(characters(index).char)
		}
		if (!inserted) b.append(value)
		b.toString
	}
	
	val selection = new Selection(this)
	val caret = new Caret(this)
	
	Listenable.listenTo(EventHandler(invalidateText, ProcessingMode.Blocking),
						font,
						text,
						size.width,
						size.height,
						kern,
						horizontalAlignment,
						verticalAlignment)
	
	listeners += EventHandler(keyPress, ProcessingMode.Blocking)
	
	override def drawComponent() = {
		selection.draw()
		caret.draw()
		preColor()		// Reset color
		super.drawComponent()
	}
	
	private def invalidateText(evt: PropertyChangeEvent[_]) = {
		lines := font()(shape, text(), kern(), size.width(), WordWrap, verticalAlignment(), horizontalAlignment())
		var chars: List[RenderedCharacter] = Nil
		for (l <- lines) {
			for (c <- l.characters) {
				chars = c :: chars
			}
		}
		characters := chars.reverse
		font() match {
			case bf: BitmapFont => texture = bf.texture
			case _ =>
		}
		
		if (evt.property == text) {
			caret.position := 0
		}
	}
	
	private def keyPress(evt: KeyPressEvent) = {
		evt.key.toUpperCase match {
			// Interaction / Selection
			case Key.Left => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) selection.end := selection.end() - 1
			} else {
				if (caret.keyboardEnabled()) {
					caret.position := (caret.position() match {
						case -1 => selection.left
						case p => p - 1 match {
							case -1 => 0
							case p => p
						}
					})
				}
			}
			case Key.Right => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) selection.end := selection.end() + 1
			} else {
				if (caret.keyboardEnabled()) {
					caret.position := (caret.position() match {
						case -1 => selection.right
						case p => p + 1
					})
				}
			}
			case Key.Home => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) selection.end := 0
			} else {
				if (caret.keyboardEnabled()) caret.position := 0
			}
			case Key.End => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) selection.end := Int.MaxValue
			} else {
				if (caret.keyboardEnabled()) caret.position := Int.MaxValue
			}
			case Key.A if (evt.controlDown) => selection.all()
			// Modification
			case Key.Backspace => {
				val p = caret.position()
				if (p != -1) {
					if (p > 0) {
						val changed = textWithout(p - 1, p - 1)
						text := changed
						caret.position(p - 1, false)
						caret.position.changed(false)
					}
				}
			}
			case Key.Delete => {
				val p = caret.position()
				if (p != -1) {
					val changed = textWithout(p, p)
					text := changed
					caret.position(p, false)
					caret.position.changed(false)
				}
			}
			case k if (!k.hasChar) => 										// Ignore non-characters
			case _ if (evt.controlDown) =>									// Ignore control-char
			case _ => {														// Insert text
				val p = caret.position()
				if (p != -1) {
					val changed = textInsert(p, evt.keyChar.toString)
					text := changed
					caret.position(p + 1, false)
					caret.position.changed(false)
				}
			}
		}
	}
}
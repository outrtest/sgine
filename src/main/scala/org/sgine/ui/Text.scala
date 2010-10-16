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
import org.sgine.render.font.NoWrap
import org.sgine.render.font.RenderedCharacter
import org.sgine.render.font.RenderedLine
import org.sgine.render.font.WordWrap

import org.sgine.scene.ext.FocusableNode

import org.sgine.ui.ext.Caret
import org.sgine.ui.ext.Selection

import org.lwjgl.opengl.GL11._

import scala.math._

class Text extends ShapeComponent with FocusableNode {
	val cull = _cull
	val font = new AdvancedProperty[Font](FontManager("Arial", 36.0), this)
	val text = new AdvancedProperty[String]("", this)
	val kern = new AdvancedProperty[Boolean](true, this)
	val textAlignment = new AdvancedProperty[HorizontalAlignment](HorizontalAlignment.Center, this)
	val editable = new AdvancedProperty[Boolean](false, this)
	val multiline = new AdvancedProperty[Boolean](true, this)
	
	protected[ui] val lines = new AdvancedProperty[Seq[RenderedLine]](Nil, this)
	protected[ui] val characters = new AdvancedProperty[Seq[RenderedCharacter]](Nil, this)
	protected[ui] def char(index: Int) = characters() match {
		case t if (t.length > index) => Some(t(index))
		case _ => None
	}
	protected[ui] def textWithout(start: Int, end: Int, replacement: String = null) = {
		val b = new StringBuilder()
		val characters = this.characters()
		for (index <- 0 until characters.length) {
			if ((index >= start) && (index <= end)) {
				// Ignore
				if ((replacement != null) && (index == start)) {
					b.append(replacement)
				}
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
						kern,
						textAlignment,
						multiline,
						clip.enabled,
						clip.x1,
						clip.y1,
						clip.x2,
						clip.y2)
	
	listeners += EventHandler(keyPress, ProcessingMode.Blocking)
	listeners += EventHandler(mousePress, ProcessingMode.Blocking)
	listeners += EventHandler(mouseMove, ProcessingMode.Blocking)
	listeners += EventHandler(mouseRelease, ProcessingMode.Blocking)
	
	override def drawComponent() = {
		selection.draw()
		caret.draw()
		preColor()		// Reset color
		super.drawComponent()
	}
	
	private def invalidateText(evt: PropertyChangeEvent[_]) = {
		val wrapMethod = multiline() match {
			case true => WordWrap
			case false => NoWrap
		}
		lines := font()(shape, text(), kern(), size.width(), wrapMethod, textAlignment())

		if (clip.enabled()) {
			org.sgine.render.shape.ShapeUtilities.clip(shape, clip.x1(), clip.y1(), clip.x2(), clip.y2())
		}
		
		var chars: List[RenderedCharacter] = Nil
		var minY = 0.0
		var maxY = 0.0
		for (l <- lines()) {
			for (c <- l.characters) {
				chars = c :: chars
				minY = min(c.y - (l.font.lineHeight / 2.0), minY)
				maxY = max(c.y + (l.font.lineHeight / 2.0), maxY)
			}
		}
		size.height := abs(minY) + abs(maxY)
		characters := chars.reverse
		font() match {
			case bf: BitmapFont => texture = bf.texture
			case _ =>
		}
		
		if (evt.property == text) {
			caret.position(0, false)
			caret.position.changed(false)
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
			case Key.Up => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) {
					val x = caret.caretX
					val y = caret.caretY + font().lineHeight
					val p = positionAtPoint(x, y)
					if (selection.end() == p) {
						selection.end := 0
					} else {
						selection.end := p
					}
				}
			} else {
				if (caret.keyboardEnabled()) {
					val x = caret.caretX
					val y = caret.caretY + font().lineHeight
					val p = positionAtPoint(x, y)
					if (caret.position() == p) {
						caret.position := 0
					} else {
						caret.position := p
					}
				}
			}
			case Key.Down => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) {
					val x = caret.caretX
					val y = caret.caretY - font().lineHeight
					val p = positionAtPoint(x, y)
					if (selection.end() == p) {
						selection.end := Int.MaxValue
					} else {
						selection.end := p
					}
				}
			} else {
				if (caret.keyboardEnabled()) {
					val x = caret.caretX
					val y = caret.caretY - font().lineHeight
					val p = positionAtPoint(x, y)
					if (caret.position() == p) {
						caret.position := Int.MaxValue
					} else {
						caret.position := p
					}
				}
			}
			case Key.Home => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) selection.end := homePosition
			} else {
				if (caret.keyboardEnabled()) caret.position := homePosition
			}
			case Key.End => if (evt.shiftDown) {
				if (selection.keyboardEnabled()) selection.end := endPosition
			} else {
				if (caret.keyboardEnabled()) caret.position := endPosition
			}
			case Key.A if (evt.controlDown) => selection.all()
			// Modification
			case Key.Backspace => if (editable()) selection.backspace()
			case Key.Delete => if (editable()) selection.delete()
			case Key.C if (evt.controlDown) => selection.copy()
			case Key.V if (evt.controlDown) => if (editable()) selection.paste()
			case Key.X if (evt.controlDown) => if (editable()) selection.cut()
			case Key.Enter if (!multiline()) => // TODO: throw ActionEvent
			case k if (!k.hasChar) => 										// Ignore non-characters
			case _ if (evt.controlDown) =>									// Ignore control-char
			case _ => {														// Insert text
				if (editable()) {
					selection.insert(evt.keyChar.toString)
				}
			}
		}
	}
	
	def homePosition = {
		val p = selection.end()
		val homeCharacter = char(p) match {
			case Some(c) => c.line.characters.head
			case None => if (p > 0) {
				char(p - 1) match {
					case Some(c) => c.line.characters.head
					case None => null
				}
			}
		}
		if (homeCharacter != null) {
			characters().indexOf(homeCharacter)
		} else {
			0
		}
	}
	
	def endPosition = {
		val p = selection.end()
		val endCharacter = char(p) match {
			case Some(c) => c.line.characters.last
			case None => if (p > 0) {
				char(p - 1) match {
					case Some(c) => c.line.characters.last
					case None => null
				}
			} else {
				null
			}
		}
		if (endCharacter != null) {
			if (endCharacter.line == lines().last) {
				characters().length
			} else {
				characters().indexOf(endCharacter)
			}
		} else {
			0
		}
	}
	
	def charAtPoint(x: Double, y: Double) = {
		var nearest: RenderedCharacter = null
		var nearestDistance = 0.0
		for (c <- characters()) {
			val distance = this.distance(c.x, c.y, x, y)
			if ((nearest == null) || (distance < nearestDistance)) {
				nearest = c
				nearestDistance = distance
			}
		}
		nearest
	}
	
	def positionAtPoint(x: Double, y: Double) = {
		val c = charAtPoint(x, y)
		val position = characters().indexOf(c)
		if (x - c.x > 0.0) {
			position + 1
		} else {
			position
		}
	}
	
	def distance(x1: Double, y1: Double, x2: Double, y2: Double) = {		// TODO: put this in a utility object
		val dx = x1 - x2
		val dy = y1 - y2
		sqrt(dx * dx + dy * dy)		// Good ol' Pythagoras
	}
	
	private var pressed = false
	private def mousePress(evt: MousePressEvent) = {
		if ((evt.button == 0) && (caret.mouseEnabled())) {
			caret.position := positionAtPoint(evt.x, evt.y)
			if (selection.mouseEnabled()) pressed = true
		}
	}
	
	private def mouseMove(evt: MouseMoveEvent) = {
		if (pressed) {		// TODO: dragging functionality would be useful?
			selection.end := positionAtPoint(evt.x, evt.y)
		}
	}
	
	private def mouseRelease(evt: MouseReleaseEvent) = {
		if (pressed) {
			selection.end := positionAtPoint(evt.x, evt.y)
			pressed = false
		}
	}
}
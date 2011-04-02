package org.sgine.ui

import org.sgine.core._

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
import org.sgine.render.font.TextBuilder
import org.sgine.render.font.WordWrap

import org.sgine.scene.ext.FocusableNode

import org.sgine.ui.ext.Caret
import org.sgine.ui.ext.Selection
import org.sgine.ui.style._

import org.lwjgl.opengl.GL11._

import scala.math._

class TextComponent extends ShapeComponent with FocusableNode {
	protected[ui] val _font = new AdvancedProperty[Font](FontManager("Arial", 36.0), this)
	protected[ui] val _text = new AdvancedProperty[String]("", this, filter = filterText)
	protected[ui] val _kern = new AdvancedProperty[Boolean](true, this)
	protected[ui] val _textColor = new AdvancedProperty[Color](Color.White, this)
	protected[ui] val _textAlignment = new AdvancedProperty[HorizontalAlignment](HorizontalAlignment.Center, this)
	protected[ui] val _editable = new AdvancedProperty[Boolean](false, this)
	protected[ui] val _multiline = new AdvancedProperty[Boolean](false, this)
	protected[ui] val _maxLength = new AdvancedProperty[Int](-1, this)
	
	protected[ui] val textBuilder = new TextBuilder()
	protected[ui] val lines = new AdvancedProperty[Seq[RenderedLine]](Nil, this)
	protected[ui] val characters = new AdvancedProperty[Seq[RenderedCharacter]](Nil, this)
	
	protected[ui] val _selection = new Selection(this)
	protected[ui] val _caret = new Caret(this)
	
	private val dirty = new java.util.concurrent.atomic.AtomicBoolean()
	
	protected[ui] def char(index: Int) = characters() match {
		case t if (t.length > index) => Some(t(index))
		case _ => None
	}
	protected[ui] def textWithout(start: Int, end: Int, replacement: String = null) = {
		val length = end - start + 1
		val value = if ((_maxLength() != -1) && (replacement != null) && (_text().length - length + replacement.length > _maxLength())) {
			val clipLength = min(replacement.length, _maxLength() - _text().length + length)
			replacement.substring(0, clipLength)
		} else {
			replacement
		}
		
		val b = new StringBuilder()
		val characters = this.characters()
		for (index <- 0 until characters.length) {
			if ((index >= start) && (index <= end)) {
				// Ignore
				if ((value != null) && (index == start)) {
					b.append(value)
				}
			} else {
				b.append(characters(index).char)
			}
		}
		b.toString -> value
	}
	protected[ui] def textInsert(position: Int, text: String) = {
		val value = if ((_maxLength() != -1) && (_text().length + text.length > _maxLength())) {
			text.substring(0, _maxLength() - _text().length)
		} else {
			text
		}
		
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
		b.toString -> value
	}
	
	private def filterText(text: String) = {
		if ((_maxLength() != -1) && (text.length > _maxLength())) {
			text.substring(0, _maxLength())
		} else {
			text
		}
	}
	
	Listenable.listenTo(EventHandler(invalidateText, ProcessingMode.Blocking),
						_font,
						_text,
						size.actual.width,
						_kern,
						_textAlignment,
						_multiline,
						clip.enabled,
						clip.x1,
						clip.y1,
						clip.x2,
						clip.y2,
						clip.adjustX,
						clip.adjustY)
	
	listeners += EventHandler(keyPress, ProcessingMode.Blocking)
	listeners += EventHandler(mousePress, ProcessingMode.Blocking)
	listeners += EventHandler(mouseMove, ProcessingMode.Blocking)
	listeners += EventHandler(mouseRelease, ProcessingMode.Blocking)
	
	override def drawComponent() = {
		if (dirty.get) {
			dirty.set(false)
			validate()
		}
		_selection.draw()
		_caret.draw()
		multColor(_textColor())
		super.drawComponent()
	}
	
	private def invalidateText(evt: PropertyChangeEvent[_]) = {
		dirty.set(true)
		
		if (evt.property == _text) {
			_caret.position(0, false)
			_caret.position.changed(false)
		}
	}
	
	private def validate() = {
		val wrapMethod = _multiline() match {
			case true => WordWrap
			case false => NoWrap
		}
		
		// Reset TextBuilder values
		textBuilder.vertices.clear()
		textBuilder.texcoords.clear()
		textBuilder.lines.clear()
		
		textBuilder.text = _text()
		textBuilder.kern = _kern()
		textBuilder.wrapWidth = size.actual.width()
		textBuilder.wrapMethod = wrapMethod
		textBuilder.textAlignment = _textAlignment()
		textBuilder.xOffset = _textAlignment() match {
			case HorizontalAlignment.Left => padding.left()
			case HorizontalAlignment.Right => padding.right()
			case _ => 0.0
		}
		if (clip.enabled()) {
			textBuilder.xOffset += clip.adjustX()
			textBuilder.yOffset += clip.adjustY()
		}
		_font().generate(textBuilder)
		lines := textBuilder.lines
		textBuilder(shape)

		if (clip.enabled()) {
			org.sgine.render.shape.ShapeUtilities.clip(shape, clip.x1(), clip.y1(), clip.x2(), clip.y2())
		}
		
		var chars: List[RenderedCharacter] = Nil
		for (l <- lines()) {
			for (c <- l.characters) {
				chars = c :: chars
			}
		}
		val measured = shape.size
		val height = textBuilder.lines.length match {
			case 0 => _font().lineHeight
			case n => _font().lineHeight * n
		}
		size.measured.width := measured.x + padding.left() + padding.right()
		size.measured.height := height + padding.bottom() + padding.top()
		characters := chars.reverse
		_font() match {
			case bf: BitmapFont => texture = bf.texture
			case _ =>
		}
	}
	
	private def keyPress(evt: KeyPressEvent) = {
		evt.key.toUpperCase match {
			// Interaction / Selection
			case Key.Left => if (evt.shiftDown) {
				if (_selection.keyboardEnabled()) _selection.end := _selection.end() - 1
			} else {
				if (_caret.keyboardEnabled()) {
					_caret.position := (_caret.position() match {
						case -1 => _selection.left
						case p => p - 1 match {
							case -1 => 0
							case p => p
						}
					})
				}
			}
			case Key.Right => if (evt.shiftDown) {
				if (_selection.keyboardEnabled()) _selection.end := _selection.end() + 1
			} else {
				if (_caret.keyboardEnabled()) {
					_caret.position := (_caret.position() match {
						case -1 => _selection.right
						case p => p + 1
					})
				}
			}
			case Key.Up => if (evt.shiftDown) {
				if (_selection.keyboardEnabled()) {
					val x = _caret.caretX
					val y = _caret.caretY + _font().lineHeight
					val p = positionAtPoint(x, y)
					if (_selection.end() == p) {
						_selection.end := 0
					} else {
						_selection.end := p
					}
				}
			} else {
				if (_caret.keyboardEnabled()) {
					val x = _caret.caretX
					val y = _caret.caretY + _font().lineHeight
					val p = positionAtPoint(x, y)
					if (_caret.position() == p) {
						_caret.position := 0
					} else {
						_caret.position := p
					}
				}
			}
			case Key.Down => if (evt.shiftDown) {
				if (_selection.keyboardEnabled()) {
					val x = _caret.caretX
					val y = _caret.caretY - _font().lineHeight
					val p = positionAtPoint(x, y)
					if (_selection.end() == p) {
						_selection.end := Int.MaxValue
					} else {
						_selection.end := p
					}
				}
			} else {
				if (_caret.keyboardEnabled()) {
					val x = _caret.caretX
					val y = _caret.caretY - _font().lineHeight
					val p = positionAtPoint(x, y)
					if (_caret.position() == p) {
						_caret.position := Int.MaxValue
					} else {
						_caret.position := p
					}
				}
			}
			case Key.Home => if (evt.shiftDown) {
				if (_selection.keyboardEnabled()) _selection.end := homePosition
			} else {
				if (_caret.keyboardEnabled()) _caret.position := homePosition
			}
			case Key.End => if (evt.shiftDown) {
				if (_selection.keyboardEnabled()) _selection.end := endPosition
			} else {
				if (_caret.keyboardEnabled()) _caret.position := endPosition
			}
			case Key.A if (evt.controlDown) => _selection.all()
			case Key.C if (evt.controlDown) => _selection.copy()
			// Modification
			case Key.Backspace => if (_editable()) _selection.backspace()
			case Key.Delete => if (_editable()) _selection.delete()
			case Key.V if (evt.controlDown) => if (_editable()) _selection.paste()
			case Key.X if (evt.controlDown) => if (_editable()) _selection.cut()
			case Key.Enter if (!_multiline()) => // TODO: throw ActionEvent
			case k if (!k.hasChar) => 										// Ignore non-characters
			case _ if (evt.controlDown) =>									// Ignore control-char
			case _ => {														// Insert text
				if (_editable()) {
					if ((_maxLength() == -1) || (_text().length < _maxLength())) {
						_selection.insert(evt.keyChar.toString)
					}
				}
			}
		}
	}
	
	def homePosition = {
		val p = _selection.end()
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
		val p = _selection.end()
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
		if ((evt.button == 0) && (_caret.mouseEnabled())) {
			_caret.position := positionAtPoint(evt.x, evt.y)
			if (_selection.mouseEnabled()) pressed = true
		}
	}
	
	private def mouseMove(evt: MouseMoveEvent) = {
		if (pressed) {		// TODO: dragging functionality would be useful?
			_selection.end := positionAtPoint(evt.x, evt.y)
		}
	}
	
	private def mouseRelease(evt: MouseReleaseEvent) = {
		if (pressed) {
			_selection.end := positionAtPoint(evt.x, evt.y)
			pressed = false
		}
	}
}
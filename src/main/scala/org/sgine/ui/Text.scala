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

class Text extends ShapeComponent with FocusableNode {
	val cull = _cull
	val font = new AdvancedProperty[Font](null, this, dependency = style.font)
	val text = new AdvancedProperty[String](null, this, filter = filterText, dependency = style.text)
	val kern = new AdvancedProperty[Boolean](false, this, dependency = style.kern)
	val textColor = new AdvancedProperty[Color](null, this, dependency = style.textColor)
	val textAlignment = new AdvancedProperty[HorizontalAlignment](null, this, dependency = style.textAlignment)
	val editable = new AdvancedProperty[Boolean](false, this, dependency = style.editable)
	val multiline = new AdvancedProperty[Boolean](false, this, dependency = style.multiline)
	val maxLength = new AdvancedProperty[Int](0, this, dependency = style.maxLength)
	
	protected[ui] val textBuilder = new TextBuilder()
	protected[ui] val lines = new AdvancedProperty[Seq[RenderedLine]](Nil, this)
	protected[ui] val characters = new AdvancedProperty[Seq[RenderedCharacter]](Nil, this)
	
	val selection = new Selection(this)
	val caret = new Caret(this)
	
	override def style: TextStyle = Text.style
	
	protected[ui] def char(index: Int) = characters() match {
		case t if (t.length > index) => Some(t(index))
		case _ => None
	}
	protected[ui] def textWithout(start: Int, end: Int, replacement: String = null) = {
		val length = end - start + 1
		val value = if ((maxLength() != -1) && (replacement != null) && (this.text().length - length + replacement.length > maxLength())) {
			val clipLength = min(replacement.length, maxLength() - this.text().length + length)
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
		val value = if ((maxLength() != -1) && (this.text().length + text.length > maxLength())) {
			text.substring(0, maxLength() - this.text().length)
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
		if ((maxLength() != -1) && (text.length > maxLength())) {
			text.substring(0, maxLength())
		} else {
			text
		}
	}
	
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
	
	cull.dependency = style.cull
	listeners += EventHandler(keyPress, ProcessingMode.Blocking)
	listeners += EventHandler(mousePress, ProcessingMode.Blocking)
	listeners += EventHandler(mouseMove, ProcessingMode.Blocking)
	listeners += EventHandler(mouseRelease, ProcessingMode.Blocking)
	
	override def drawComponent() = {
		selection.draw()
		caret.draw()
		multColor(textColor())
		super.drawComponent()
	}
	
	private def invalidateText(evt: PropertyChangeEvent[_]) = {
		val wrapMethod = multiline() match {
			case true => WordWrap
			case false => NoWrap
		}
		
		// Reset TextBuilder values
		textBuilder.vertices.clear()
		textBuilder.texcoords.clear()
		textBuilder.lines.clear()
		
		textBuilder.text = text()
		textBuilder.kern = kern()
		textBuilder.wrapWidth = size.width()
		textBuilder.wrapMethod = wrapMethod
		textBuilder.textAlignment = textAlignment()
		textBuilder.xOffset = padding.left()
		font().generate(textBuilder)
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
		val height = textBuilder.lines.length match {
			case 0 => font().lineHeight
			case n => font().lineHeight * n
		}
		size.height := height + padding.bottom() + padding.top()
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
			case Key.C if (evt.controlDown) => selection.copy()
			// Modification
			case Key.Backspace => if (editable()) selection.backspace()
			case Key.Delete => if (editable()) selection.delete()
			case Key.V if (evt.controlDown) => if (editable()) selection.paste()
			case Key.X if (evt.controlDown) => if (editable()) selection.cut()
			case Key.Enter if (!multiline()) => // TODO: throw ActionEvent
			case k if (!k.hasChar) => 										// Ignore non-characters
			case _ if (evt.controlDown) =>									// Ignore control-char
			case _ => {														// Insert text
				if (editable()) {
					if ((maxLength() == -1) || (text().length < maxLength())) {
						selection.insert(evt.keyChar.toString)
					}
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

object Text {
	val style = new TextStyle {
		val alpha = StyleProperty(0.0, this, Component.style.alpha)
		val color = StyleProperty(null, this, Component.style.color)
		val cull = StyleProperty[Face](Face.Back, this)
		val font = StyleProperty(FontManager("Arial", 36.0), this)
		val text = StyleProperty("", this)
		val kern = StyleProperty(true, this)
		val textColor = StyleProperty(Color.White, this)
		val textAlignment = StyleProperty[HorizontalAlignment](HorizontalAlignment.Center, this)
		val editable = StyleProperty(false, this)
		val multiline = StyleProperty(false, this)
		val maxLength = StyleProperty(-1, this)
		val caret = new CaretStyle {
			val visible = StyleProperty(true, this)
			val width = StyleProperty(1.0, this)
			val color = StyleProperty(Color.White, this)
			val rate = StyleProperty(0.5, this)
			val mouseEnabled = StyleProperty(true, this)
			val keyboardEnabled = StyleProperty(true, this)
		}
		val selection = new SelectionStyle {
			val visible = StyleProperty(true, this)
			val color = StyleProperty(Color.SelectBlue.set(alpha = 0.5), this)
			val mouseEnabled = StyleProperty(true, this)
			val keyboardEnabled = StyleProperty(true, this)
		}
	}
}

trait TextStyle extends ComponentStyle {
	val cull: StyleProperty[Face]
	val font: StyleProperty[Font]
	val text: StyleProperty[String]
	val kern: StyleProperty[Boolean]
	val textColor: StyleProperty[Color]
	val textAlignment: StyleProperty[HorizontalAlignment]
	val editable: StyleProperty[Boolean]
	val multiline: StyleProperty[Boolean]
	val maxLength: StyleProperty[Int]
	val caret: CaretStyle
	val selection: SelectionStyle
	
	trait CaretStyle extends Style {
		val visible: StyleProperty[Boolean]
		val width: StyleProperty[Double]
		val color: StyleProperty[Color]
		val rate: StyleProperty[Double]
		val mouseEnabled: StyleProperty[Boolean]
		val keyboardEnabled: StyleProperty[Boolean]
	}
	
	trait SelectionStyle extends Style {
		val visible: StyleProperty[Boolean]
		val color: StyleProperty[Color]
		val mouseEnabled: StyleProperty[Boolean]
		val keyboardEnabled: StyleProperty[Boolean]
	}
}
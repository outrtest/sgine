package org.sgine.ui

import org.sgine.core._

import org.sgine.event.ActionEvent
import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.input.Key
import org.sgine.input.event._

import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

class Button extends TextComponent with SkinnedComponent {
	val text = _text
	val textAlignment = _textAlignment
	val textColor = _textColor
	val font = _font
	
	_multiline := false
	_editable := false
	_selection.visible := false
	_caret.visible := false
	
	def this(text: String) = {
		this()
		
		_text := text
	}
	
	listeners += EventHandler(mouseClicked, ProcessingMode.Blocking)
	listeners += EventHandler(keyPressed, ProcessingMode.Blocking, filter = keyActionFilter _)
	listeners += EventHandler(keyReleased, ProcessingMode.Blocking, filter = keyActionFilter _)
	listeners += EventHandler(keyTyped, ProcessingMode.Blocking, filter = keyActionFilter _)
	
	private def mouseClicked(evt: MouseClickEvent) = {
		val action = ActionEvent(this, "click")
		Event.enqueue(action)
	}
	
	private def keyPressed(evt: KeyPressEvent) = {
		statePress.activate()
	}
	
	private def keyReleased(evt: KeyReleaseEvent) = {
		statePress.deactivate()
	}
	
	private def keyTyped(evt: KeyTypeEvent) = {
		val action = ActionEvent(this, "click")
		Event.enqueue(action)
	}
	
	private def keyActionFilter(evt: KeyEvent) = evt.key match {
		case Key.Space if (!evt.repeat) => true
		case Key.Enter if (!evt.repeat) => true
		case _ => false
	}
}
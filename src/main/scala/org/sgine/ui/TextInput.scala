package org.sgine.ui

import org.sgine.core._

import org.sgine.event._

import org.sgine.input.Key
import org.sgine.input.event._

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

import org.sgine.ui.style._

class TextInput extends TextComponent with SkinnedComponent {
	val text = _text
	val editable = _editable
	val textAlignment = _textAlignment
	val font = _font
	val textColor = _textColor
	val caret = _caret
	
	_multiline := false
	
	listeners += EventHandler(keyTyped, ProcessingMode.Blocking, filter = keyActionFilter _)
	focused.listeners += EventHandler(focusChanged, ProcessingMode.Blocking)
	
	private def keyTyped(evt: KeyTypeEvent) = {
		val action = ActionEvent(this, "submit")
		Event.enqueue(action)
	}
	
	private def keyActionFilter(evt: KeyEvent) = evt.key match {
		case Key.Enter if (!evt.repeat) => true
		case _ => false
	}
	
	private def focusChanged(evt: PropertyChangeEvent[Boolean]) = {
		if (evt.newValue) {
			if (evt.cause.isInstanceOf[KeyEvent]) {
				_selection.all()
			}
		} else {
			_selection.none()
		}
	}
}
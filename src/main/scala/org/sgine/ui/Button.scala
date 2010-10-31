package org.sgine.ui

import org.sgine.core._

import org.sgine.event.ActionEvent
import org.sgine.event.Event
import org.sgine.event.EventHandler

import org.sgine.input.Key
import org.sgine.input.event._

import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

import org.sgine.scene.ext.FocusableNode

import org.sgine.ui.layout.BoxLayout

class Button extends AbstractContainer with SkinnedComponent with FocusableNode {
	private val textComponent = new TextComponent()
	
	val text = textComponent._text
	val textAlignment = textComponent._textAlignment
	val textColor = textComponent._textColor
	val font = textComponent._font
	
	textComponent.size.width.mode := SizeMode.Auto
	textComponent.size.height.mode := SizeMode.Auto
	textComponent._multiline := false
	textComponent._editable := false
	textComponent._selection.visible := false
	textComponent._caret.visible := false
	textComponent.focusable := false
	
	_layout := BoxLayout()
	this += textComponent
	
	def this(text: String) = {
		this()
		
		this.text := text
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
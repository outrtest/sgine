package org.sgine.ui

import org.sgine.core._

import org.sgine.event.ActionEvent
import org.sgine.event.Event
import org.sgine.event.EventHandler
import org.sgine.event.Listenable

import org.sgine.input.Key
import org.sgine.input.event._

import org.sgine.property.AdvancedProperty
import org.sgine.property.NumericProperty
import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

import org.sgine.scene.ext.FocusableNode

import org.sgine.ui.layout.BoxLayout

class Button extends AbstractContainer with SkinnedComponent with FocusableNode {
	private val image = new Image()
	private val textComponent = new TextComponent()
	
	val text = textComponent._text
	val textAlignment = textComponent._textAlignment
	val textColor = textComponent._textColor
	val font = textComponent._font
	
	val icon = image.source
	val iconSpacing = new NumericProperty(5.0, this)
	val iconPlacement = new AdvancedProperty[Placement](Placement.Top, this)
	
	textComponent.size.width.mode := SizeMode.Auto
	textComponent.size.height.mode := SizeMode.Auto
	textComponent._multiline := false
	textComponent._editable := false
	textComponent._selection.visible := false
	textComponent._caret.visible := false
	textComponent.focusable := false
	
	changeLayout(null)
	this += image
	this += textComponent
	
	def this(text: String) = {
		this()
		
		this.text := text
	}
	
	listeners += EventHandler(mouseClicked, ProcessingMode.Blocking)
	listeners += EventHandler(keyPressed, ProcessingMode.Blocking, filter = keyActionFilter _)
	listeners += EventHandler(keyReleased, ProcessingMode.Blocking, filter = keyActionFilter _)
	listeners += EventHandler(keyTyped, ProcessingMode.Blocking, filter = keyActionFilter _)
	
	Listenable.listenTo(EventHandler(changeLayout, ProcessingMode.Blocking), iconSpacing, iconPlacement)
	
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
	
	private def changeLayout(evt: PropertyChangeEvent[_]) = {
		val direction = iconPlacement() match {
			case Placement.Left => Direction.Horizontal
			case Placement.Right => Direction.Horizontal
			case _ => Direction.Vertical
		}
		val reverse = iconPlacement() match {
			case Placement.Right => true
			case Placement.Bottom => true
			case _ => false
		}
		_layout := BoxLayout(direction, iconSpacing(), reverse)
	}
}
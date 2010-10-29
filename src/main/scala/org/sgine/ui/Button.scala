package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.input.event._

import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

class Button extends Text with SkinnedComponent {
	def this(text: String) = {
		this()
		
		this.text := text
	}
	
	configureButton()
	
	protected def configureButton() = {
		// TODO: move to theme
		multiline := false
		editable := false
		textAlignment := org.sgine.core.HorizontalAlignment.Center
		selection.visible := false
		caret.visible := false
		font := org.sgine.render.font.FontManager("Arial", 24.0)
		val scale9 = new org.sgine.ui.skin.Scale9Skin()
		scale9(org.sgine.core.Resource("scale9/windows/button/normal.png"), 3.0, 3.0, 4.0, 5.0)
		skin := scale9
		textColor := org.sgine.core.Color.Black
		padding(10.0, 25.0, 10.0, 25.0)
		
//		val hoverState = new State("hoverSkin")
//		hoverState.add("skin().source", Resource("scale9/windows/button/hover.png"))
//		states += hoverState
//		
//		val focusState = new State("focusSkin")
//		focusState.add("skin().source", Resource("scale9/windows/button/pressed.png"))
//		states += focusState
//		
//		val pressedState = new State("pressedSkin")
//		pressedState.add("skin().source", Resource("scale9/windows/button/focused.png"))
//		states += pressedState
//		
//		onEvent[MouseOverEvent](org.sgine.core.ProcessingMode.Blocking) {
//			if (!focused()) {
//				states.activate("hoverSkin")
//			}
//		}
//		onEvent[MouseOutEvent](org.sgine.core.ProcessingMode.Blocking) {
//			states.deactivate("hoverSkin")
//		}
//		onEvent[MousePressEvent](org.sgine.core.ProcessingMode.Blocking) {
//			states.deactivate("hoverSkin")
//			states.deactivate("focusSkin")
//			states.activate("pressedSkin")
//		}
//		onEvent[MouseReleaseEvent](org.sgine.core.ProcessingMode.Blocking) {
//			states.deactivate("pressedSkin")
//			if (focused()) {
//				states.activate("focusSkin")
//			} else {
//				states.activate("hoverSkin")
//			}
//		}
//		focused.onEvent[PropertyChangeEvent[_]](org.sgine.core.ProcessingMode.Blocking) {
//			if (focused()) {
//				states.deactivate("hoverSkin")
//				states.activate("focusSkin")
//			} else {
//				states.deactivate("focusSkin")
//				if (mouseState()) {
//					states.activate("hoverSkin")
//				}
//			}
//		}
	}
}
package org.sgine.ui

import org.sgine.core._

import org.sgine.input.event.MouseOutEvent
import org.sgine.input.event.MouseOverEvent

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer
import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

import org.sgine.ui.style._

class TextInput extends Text with SkinnedComponent {
	configureText()
	
	protected def configureText() = {
		// TODO: FIX STYLE SUPPORT AND PUT THIS IN THEMING
		val hoverState = new State("hoverSkin")
		hoverState.add("skin().source", Resource("scale9/windows/textinput/hover.png"))
		states += hoverState
		
		val focusState = new State("focusSkin")
		focusState.add("skin().source", Resource("scale9/windows/textinput/focused.png"))
		states += focusState
		
		onEvent[MouseOverEvent](org.sgine.core.ProcessingMode.Blocking) {
			if (!focused()) {
				states.activate("hoverSkin")
			}
		}
		onEvent[MouseOutEvent](org.sgine.core.ProcessingMode.Blocking) {
			states.deactivate("hoverSkin")
		}
		focused.onEvent[PropertyChangeEvent[_]](org.sgine.core.ProcessingMode.Blocking) {
			if (focused()) {
				states.deactivate("hoverSkin")
				states.activate("focusSkin")
//				selection.all()
			} else {
				states.deactivate("focusSkin")
				if (mouseState()) {
					states.activate("hoverSkin")
				}
//				selection.none()
			}
		}
	}
}
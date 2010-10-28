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
//		size.width := 300.0
//		size.width.mode := SizeMode.Explicit
//		multiline := false
//		editable := true
//		textAlignment := HorizontalAlignment.Left
//		font := org.sgine.render.font.FontManager("Arial", 24.0)
//		val scale9 = new org.sgine.ui.skin.Scale9Skin()
//		scale9(Resource("scale9/windows/textinput/normal.png"), 2.0, 2.0, 3.0, 3.0)
//		skin := scale9
		textColor := org.sgine.core.Color.Black
		caret.color := org.sgine.core.Color.Black
		clip.enabled := true
		clip.x1 := -150.0
		clip.x2 := 150.0
		clip.y1 := -200.0
		clip.y2 := 200.0
		padding(5.0)
		
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
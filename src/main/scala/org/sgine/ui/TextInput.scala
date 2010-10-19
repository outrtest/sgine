package org.sgine.ui

import org.sgine.core.Face
import org.sgine.core.HorizontalAlignment
import org.sgine.core.Resource

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.style._

class TextInput extends Text with SkinnedComponent {
	override def style: TextInputStyle = TextInput.style
	
	configureText()
	
	protected def configureText() = {
		size.width := 300.0
		
		font := org.sgine.render.font.FontManager("Arial", 24.0)
		val scale9 = new org.sgine.ui.skin.Scale9Skin()
		scale9(Resource("scale9/windows/textinput/focused.png"), 2.0, 2.0, 3.0, 3.0)
		skin := scale9
		textColor := org.sgine.core.Color.Blue
		caret.color := org.sgine.core.Color.Black
		clip.enabled := true
		clip.x1 := -150.0
		clip.x2 := 150.0
		clip.y1 := -200.0
		clip.y2 := 200.0
		padding(5.0)
	}
}

object TextInput {
	val style = new TextInputStyle {
		val alpha = StyleProperty(0.0, this, Text.style.alpha)
		val color = StyleProperty(null, this, Text.style.color)
		val cull = StyleProperty[Face](null, this, Text.style.cull)
		val font = StyleProperty(null, this, Text.style.font)
		val text = StyleProperty(null, this, Text.style.text)
		val kern = StyleProperty(true, this, Text.style.kern)
		val textColor = StyleProperty(null, this, Text.style.textColor)
		val textAlignment = StyleProperty(null, this, Text.style.textAlignment)
		val editable = StyleProperty(false, this, Text.style.editable)
		val multiline = StyleProperty(false, this, Text.style.multiline)
		val maxLength = StyleProperty(0, this, Text.style.maxLength)
		val caret = new CaretStyle {
			val visible = StyleProperty(false, this, Text.style.caret.visible)
			val width = StyleProperty(0.0, this, Text.style.caret.width)
			val color = StyleProperty(null, this, Text.style.caret.color)
			val rate = StyleProperty(0.0, this, Text.style.caret.rate)
			val mouseEnabled = StyleProperty(false, this, Text.style.caret.mouseEnabled)
			val keyboardEnabled = StyleProperty(false, this, Text.style.caret.keyboardEnabled)
		}
		val selection = new SelectionStyle {
			val visible = StyleProperty(false, this, Text.style.selection.visible)
			val color = StyleProperty(null, this, Text.style.selection.color)
			val mouseEnabled = StyleProperty(false, this, Text.style.selection.mouseEnabled)
			val keyboardEnabled = StyleProperty(false, this, Text.style.selection.keyboardEnabled)
		}
		
		// Set defaults
		// TODO: put this in themes?
		multiline := false
		editable := true
		textAlignment := HorizontalAlignment.Left
	}
}

trait TextInputStyle extends TextStyle
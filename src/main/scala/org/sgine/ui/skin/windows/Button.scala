package org.sgine.ui.skin.windows

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.ui.Label
import org.sgine.ui.SkinnableComponent
import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent

class Button extends SkinnableComponent with AdvancedComponent with PaddingComponent {
	protected val normalResource = Resource("scale9/windows/button/normal.png")
	protected val hoverResource = Resource("scale9/windows/button/hover.png")
	protected val pressedResource = Resource("scale9/windows/button/pressed.png")
	protected val focusedResource = Resource("scale9/windows/button/focused.png")

	protected val face: Label = new Label()
	face.text := "Hello World"
	face.color := Color.Black
	
	protected val skinX1 = 3.0
	protected val skinY1 = 3.0
	protected val skinX2 = 4.0
	protected val skinY2 = 5.0
}
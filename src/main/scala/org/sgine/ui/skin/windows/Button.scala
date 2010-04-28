package org.sgine.ui.skin

import org.sgine.core.Resource

import org.sgine.ui.Label
import org.sgine.ui.SkinnableComponent

class Button extends SkinnableComponent {
	protected val normalResource = Resource("scale9/windows/button/normal.png")
	protected val hoverResource = Resource("scale9/windows/button/hover.png")
	protected val pressedResource = Resource("scale9/windows/button/pressed.png")
	protected val focusedResource = Resource("scale9/windows/button/focused.png")

	protected val face: Label = new Label()
	
	protected val skinX1 = 3.0
	protected val skinY1 = 3.0
	protected val skinX2 = 4.0
	protected val skinY2 = 5.0
}
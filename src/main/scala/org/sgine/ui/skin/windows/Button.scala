package org.sgine.ui.skin.windows

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.event._

import org.sgine.property.AdvancedProperty

import org.sgine.render.font.Font
import org.sgine.render.font.FontManager

import org.sgine.ui.Label
import org.sgine.ui.SkinnableComponent
import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent

class Button extends SkinnableComponent with AdvancedComponent with PaddingComponent {
	protected val normalResource = Resource("scale9/windows/button/normal.png")
	protected val hoverResource = Resource("scale9/windows/button/hover.png")
	protected val pressedResource = Resource("scale9/windows/button/pressed.png")
	protected val focusedResource = Resource("scale9/windows/button/focused.png")

	private var pressed: Boolean = false
	private var over: Boolean = false
	
	protected val face: Label = new Label()
	
	protected val skinX1 = 3.0
	protected val skinY1 = 3.0
	protected val skinX2 = 4.0
	protected val skinY2 = 5.0
	
	padding.top := 10.0
	padding.bottom := 10.0
	padding.left := 25.0
	padding.right := 25.0
	
	val font = new AdvancedProperty[Font](FontManager("Arial32"), this)
	val text = new AdvancedProperty[String]("", this)
	val textColor = new AdvancedProperty[Color](Color.Black, this)
	
	configureBindings()
	configureListeners()
	
	protected def configureBindings() = {
		face.font bind font
		face.text bind text
		face.color bind textColor
	}
	
	private def configureListeners() = {
		face.listeners += EventHandler(updateBounding, ProcessingMode.Blocking)
	}
}
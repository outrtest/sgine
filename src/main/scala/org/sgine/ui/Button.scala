package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.input.event._

import org.sgine.property.AdvancedProperty

import org.sgine.render.font.Font
import org.sgine.render.font.FontManager

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent
import org.sgine.ui.style.Theme

class Button extends SkinnableComponent with AdvancedComponent with PaddingComponent {
	protected val normalResource = Theme.current.buttonNormalSkin
	protected val hoverResource = Theme.current.buttonHoverSkin
	protected val pressedResource = Theme.current.buttonPressedSkin
	protected val focusedResource = Theme.current.buttonFocusSkin

	private var pressed: Boolean = false
	private var over: Boolean = false
	
	protected val face: Label = new Label()
	
	protected val skinX1 = Theme.current.buttonSkinX1
	protected val skinY1 = Theme.current.buttonSkinY1
	protected val skinX2 = Theme.current.buttonSkinX2
	protected val skinY2 = Theme.current.buttonSkinY2
	
	padding.top := Theme.current.buttonPaddingTop
	padding.bottom := Theme.current.buttonPaddingBottom
	padding.left := Theme.current.buttonPaddingLeft
	padding.right := Theme.current.buttonPaddingRight
	
	val font = new AdvancedProperty[Font](Theme.current.font, this)
	val text = new AdvancedProperty[String]("", this)
	val textColor = new AdvancedProperty[Color](Theme.current.textColor, this)
	
	configureBindings()
	configureListeners()
	
	protected def configureBindings() = {
		face.location.z := 0.00001
		face.font bind font
		face.text bind text
		face.color bind textColor
	}
	
	private def configureListeners() = {
		face.listeners += EventHandler(updateBounding, ProcessingMode.Blocking)
	}
}
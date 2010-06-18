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
	protected val normalResource = Theme.button.normalSkin()
	protected val hoverResource = Theme.button.hoverSkin()
	protected val pressedResource = Theme.button.pressedSkin()
	protected val focusedResource = Theme.button.focusedSkin()

	private var pressed: Boolean = false
	private var over: Boolean = false
	
	protected val face: Label = new Label()
	
	protected val skinX1 = Theme.button.skinX1()
	protected val skinY1 = Theme.button.skinY1()
	protected val skinX2 = Theme.button.skinX2()
	protected val skinY2 = Theme.button.skinY2()
	
	padding.top := Theme.button.paddingTop()
	padding.bottom := Theme.button.paddingBottom()
	padding.left := Theme.button.paddingLeft()
	padding.right := Theme.button.paddingRight()
	
	val font = new AdvancedProperty[Font](Theme.font(), this)
	val text = new AdvancedProperty[String]("", this)
	val textColor = new AdvancedProperty[Color](Theme.textColor(), this)
	
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
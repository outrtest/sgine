package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.render.font.Font

import org.sgine.ui.ext.AdvancedComponent
import org.sgine.ui.ext.PaddingComponent
import org.sgine.ui.style.Theme

class TextInput extends SkinnableComponent with AdvancedComponent with PaddingComponent {
	protected val normalResource = TextInput.normalSkin()
	protected val hoverResource = TextInput.hoverSkin()
	protected val focusedResource = TextInput.focusedSkin()
	
	protected val face = new Label()
	
	protected val skinX1 = TextInput.skinX1()
	protected val skinY1 = TextInput.skinY1()
	protected val skinX2 = TextInput.skinX2()
	protected val skinY2 = TextInput.skinY2()
	
	padding.top := TextInput.paddingTop()
	padding.bottom := TextInput.paddingBottom()
	padding.left := TextInput.paddingLeft()
	padding.right := TextInput.paddingRight()
	
	val font = new AdvancedProperty[Font](null, this, null, Theme.font)
	val text = new AdvancedProperty[String]("", this)
	val textColor = new AdvancedProperty[Color](null, this, null, Theme.textColor)
	
	configureBindings()
	configureListeners()
	
	def this(text: String) = {
		this()
		
		this.text := text
	}
	
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

object TextInput extends PropertyContainer with ListenableProperty[Int] {
	val normalSkin = new AdvancedProperty[Resource](null, this)
	val hoverSkin = new AdvancedProperty[Resource](null, this)
	val focusedSkin = new AdvancedProperty[Resource](null, this)
	
	val skinX1 = new AdvancedProperty[Double](0.0, this)
	val skinY1 = new AdvancedProperty[Double](0.0, this)
	val skinX2 = new AdvancedProperty[Double](0.0, this)
	val skinY2 = new AdvancedProperty[Double](0.0, this)
	
	val paddingTop = new AdvancedProperty[Double](0.0, this)
	val paddingBottom = new AdvancedProperty[Double](0.0, this)
	val paddingLeft = new AdvancedProperty[Double](0.0, this)
	val paddingRight = new AdvancedProperty[Double](0.0, this)
	
	parent = Theme
}
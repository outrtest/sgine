package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.HorizontalAlignment
import org.sgine.core.ProcessingMode
import org.sgine.core.Resource

import org.sgine.event.EventHandler

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.render.font.Font

import org.sgine.ui.style.Theme

class TextInputOld extends SkinnableComponent {
	protected val normalResource = TextInputOld.normalSkin()
	protected val hoverResource = TextInputOld.hoverSkin()
	protected val focusedResource = TextInputOld.focusedSkin()
	
	protected val face = createFace()
	
	protected val skinX1 = TextInputOld.skinX1()
	protected val skinY1 = TextInputOld.skinY1()
	protected val skinX2 = TextInputOld.skinX2()
	protected val skinY2 = TextInputOld.skinY2()
	
	padding.top := TextInputOld.paddingTop()
	padding.bottom := TextInputOld.paddingBottom()
	padding.left := TextInputOld.paddingLeft()
	padding.right := TextInputOld.paddingRight()
	
	val font = new AdvancedProperty[Font](null, this, null, Theme.font)
	val text = new AdvancedProperty[String]("", this)
	val textColor = new AdvancedProperty[Color](null, this, null, Theme.textColor)
	
	configureBindings()
	configureListeners()
	
	def this(text: String) = {
		this()
		
		this.text := text
	}
	
	protected def createFace() = {
		val t = new Text()
		
		t.multiline := false
		t.textAlignment := HorizontalAlignment.Left
		t.size.width := 200.0
		
		t
	}
	
	protected def configureBindings() = {
		face.location.z := 0.00001
		
		face.font bind font
		face.text bind text
		face.color bind textColor
	}
	
	private def configureListeners() = {
		face.bounding.listeners += EventHandler(updateBounding, ProcessingMode.Blocking)
	}
}

object TextInputOld extends PropertyContainer with ListenableProperty[Int] {
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
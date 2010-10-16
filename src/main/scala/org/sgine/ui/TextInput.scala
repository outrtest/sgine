package org.sgine.ui

import org.sgine.core.HorizontalAlignment
import org.sgine.core.Resource

import org.sgine.property.AdvancedProperty
import org.sgine.property.ListenableProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.style.Theme

class TextInput extends Text {
	configureText()
	
	protected def configureText() = {
		multiline := false
		editable := true
		textAlignment := HorizontalAlignment.Left
		
		size.width := 200.0
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
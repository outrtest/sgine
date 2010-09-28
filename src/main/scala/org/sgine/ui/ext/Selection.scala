package org.sgine.ui.ext

import org.sgine.property.AdvancedProperty
import org.sgine.property.container.PropertyContainer

import org.sgine.ui.Text

class Selection(override val parent: Text) extends PropertyContainer {
	val visible = new AdvancedProperty[Boolean](true, this)
	val begin = new AdvancedProperty[Int](0, this)
	val end = new AdvancedProperty[Int](0, this)
	val mouseEnabled = new AdvancedProperty[Boolean](true, this)
	val keyboardEnabled = new AdvancedProperty[Boolean](true, this)
}
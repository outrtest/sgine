package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.input.event._

import org.sgine.property.event.PropertyChangeEvent
import org.sgine.property.state._

class Button extends Text with SkinnedComponent {
	def this(text: String) = {
		this()
		
		this.text := text
	}
}
package org.sgine.ui

import org.sgine.ui.ext.Clip

trait ClippableComponent {
	this: Component =>
	
	val clip = new Clip(this)
}
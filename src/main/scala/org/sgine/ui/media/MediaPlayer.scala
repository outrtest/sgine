package org.sgine.ui.media

import org.sgine.bounding.BoundingObject
import org.sgine.bounding.mutable.BoundingQuad

import org.sgine.property.ListenableProperty
import org.sgine.property.MutableProperty

import org.sgine.render.{Image => RenderImage}

import org.sgine.ui.ext.AdvancedComponent

class MediaPlayer extends AdvancedComponent with BoundingObject {
	val renderImage = new MutableProperty[RenderImage](null) with ListenableProperty[RenderImage]
	
	protected val _bounding = BoundingQuad()
	
	def drawComponent() = {
		if (renderImage() != null) {
			renderImage().draw()
		}
	}
}
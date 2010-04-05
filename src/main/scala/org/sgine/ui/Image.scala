package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.{Image => RenderImage, TextureUtil}

import org.sgine.ui.ext.LocationComponent

class Image extends Component with LocationComponent {
	private var image: RenderImage = _
	
	val source = new AdvancedProperty[Resource](null, this)
	
	configureListeners()
	
	def drawComponent() = {
		if (image != null) {
			image.draw()
		}
	}
	
	private def configureListeners() = {
		source.listeners += EventHandler(sourceChanged, ProcessingMode.Blocking)
	}
	
	private def sourceChanged(evt: PropertyChangeEvent[Resource]) = {
		val t = TextureUtil(source().url)
		image = RenderImage(t)
	}
}
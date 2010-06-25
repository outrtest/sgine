package org.sgine.ui.event

import org.sgine.event.BasicEvent

import org.sgine.ui.Image

class ImageUpdateEvent private(val image: Image) extends BasicEvent(image)

object ImageUpdateEvent {
	def apply(image: Image) = new ImageUpdateEvent(image)
}
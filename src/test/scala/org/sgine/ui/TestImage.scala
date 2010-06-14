package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.UIDisplay

object TestImage extends UIDisplay {
	def main(args: Array[String]): Unit = {
		val component = new Image()
		component.source := Resource("puppies.jpg")
		scene += component
		
		start("Test Image")
	}
}
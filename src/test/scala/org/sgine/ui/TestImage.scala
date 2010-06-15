package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.StandardDisplay

object TestImage extends StandardDisplay {
	def main(args: Array[String]): Unit = {
		val component = new Image(Resource("puppies.jpg"))
		scene += component
		start()
	}
}
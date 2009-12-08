package org.sgine.opengl.scene

import org.sgine.property._
import org.sgine.opengl.shape._

class GLShape extends GLSpatial {
	lazy val shape = new AdvancedProperty[Shape]
	
	def render(time: Double) = {
		val s = shape()
		if (s != null) {
			s(time)
		}
	}
}
package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

/**
 * Simple display of a ui.Image component.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
object TestImage extends StandardDisplay with Debug {
	def setup() = {
		val component = new Image(Resource("puppies.jpg"))
		scene += component
	}
	
	private def test(): Unit = {
		org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL)
	}
}
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
		renderer.invokeLater(test _)
		renderer.light0.enabled := true
		renderer.light0.ambience := org.sgine.core.Color(0.2, 0.3, 0.6, 1.0)
		renderer.light0.diffuse := org.sgine.core.Color(0.2, 0.3, 0.6, 1.0)
		
		val component = new Image(Resource("puppies.jpg"))
		scene += component
	}
	
	private def test(): Unit = {
		org.lwjgl.opengl.GL11.glEnable(org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL)
	}
}
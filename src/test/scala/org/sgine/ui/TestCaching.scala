package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.CacheableRenderable
import org.sgine.render.StandardDisplay

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestCaching extends StandardDisplay {
	override val scene = new GeneralNodeContainer() with CacheableRenderable with ResolutionNode
	
	def setup() = {
		val component = new Image()
		component.source := Resource("puppies.jpg")
		component.color := Color(1.0, 1.0, 1.0, 0.5)
		scene += component
	}
	
	override def main(args: Array[String]): Unit = {
		super.main(args)
		
		Thread.sleep(5000)
		scene.cache := true
	}
}
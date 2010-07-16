package org.sgine.ui

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.CacheableRenderable
import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestCaching extends StandardDisplay with Debug {
	val container = new GeneralNodeContainer() with CacheableRenderable
	
	def setup() = {
		scene += container
		
		val component = new Image()
		component.source := Resource("puppies.jpg")
		component.color := Color(1.0, 1.0, 1.0, 0.5)
		container += component
	}
	
	override def main(args: Array[String]): Unit = {
		super.main(args)
		
		Thread.sleep(5000)
		container.cache := true
	}
}
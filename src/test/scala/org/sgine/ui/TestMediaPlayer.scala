package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

object TestMediaPlayer {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test MediaPlayer")
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(640, 480)
		
		val component = new MediaPlayer()
		val url = new java.net.URL("http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_2956241001_big-buck-bunny-640x360.flv")
		component.source := Resource(url)
		scene += component
		
		r.renderable := RenderableScene(scene)
		
		component.play()
	}
}
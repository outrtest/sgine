package org.sgine.ui

import org.sgine.core.Resource

import org.sgine.render.Debug
import org.sgine.render.StandardDisplay

object TestMediaPlayer extends StandardDisplay with Debug {
	def setup() = {
		renderer.verticalSync := false
		
		val component = new MediaPlayer()
		val url = new java.net.URL("http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_2956241001_big-buck-bunny-640x360.flv")
		component.source := Resource(url)
		scene += component
		
		component.play()
	}
}
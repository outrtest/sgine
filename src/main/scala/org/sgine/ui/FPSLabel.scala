package org.sgine.ui

import org.sgine.render.Renderer

import org.sgine.work.Updatable

class FPSLabel extends Label {
	private var time = 0.0
	private var count = 0
	private var framerate = 0
	
	override def drawComponent() = {
		time += Renderer.time.get
		count += 1
		framerate += Renderer.fps.get
		if (time >= 1.0) {
			val fps = framerate / count
			text := fps + " fps - Max: " + Renderer.maxTime.get
			Renderer.maxTime.set(0.0)
			
			time = 0.0
			count = 0
			framerate = 0
		}
		
		super.drawComponent()
	}
}
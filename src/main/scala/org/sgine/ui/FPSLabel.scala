package org.sgine.ui

import org.sgine.render.Renderer

import org.sgine.work.Updatable

import org.lwjgl.opengl.GL11._

class FPSLabel extends Label {
	private var time = 0.0
	private var count = 0
	private var framerate = 0
	
	useCamera := false
	lighting := false
	
	override def drawComponent() = {
		time += Renderer().time
		count += 1
		framerate += Renderer().fps
		if (time >= 1.0) {
			val fps = framerate / count
			text := fps + " fps"
			
			time = 0.0
			count = 0
			framerate = 0
		}
		
		super.drawComponent()
	}
}
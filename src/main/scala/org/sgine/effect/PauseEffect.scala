package org.sgine.effect

import org.sgine.work.Updatable

class PauseEffect(duration: Double) extends Effect with Updatable {
	private var running: Boolean = false
	private var elapsed: Double = 0.0
	
	def play() = {
		elapsed = 0.0
		running = true
	}
	
	def finished = running
	
	override def update(time: Double) = {
		if (running) {
			elapsed += time
			
			if (elapsed >= duration) {
				running = false
			}
		}
		
		super.update(time)
	}
}
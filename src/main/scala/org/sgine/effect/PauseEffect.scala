package org.sgine.effect

import org.sgine.work.Updatable

class PauseEffect(duration: Double) extends Effect with Updatable {
	private var running: Boolean = false
	private var elapsed: Double = 0.0
	private var isFinished: Boolean = false
	
	def play() = {
		initUpdatable()
		
		elapsed = 0.0
		running = true
		isFinished = false
	}
	
	def finished = isFinished
	
	override def update(time: Double) = {
		if (running) {
			elapsed += time
			
			if (elapsed >= duration) {
				elapsed = 0.0
				running = false
				isFinished = true
			}
		}
		
		super.update(time)
	}
}
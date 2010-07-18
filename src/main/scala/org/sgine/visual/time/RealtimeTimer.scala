package org.sgine.visual.time

class RealtimeTimer(resolution: Double = 1.0) extends Timer {
	private var _time = 0.0
	private var _fps = 0.0
	
	def time = _time
	
	def fps = _fps
	
	def update(time: Double) = {
		_fps = 1.0 / time
		_time = time
	}
}
package org.sgine.visual.time

import org.sgine.Updatable

trait Timer extends Updatable {
	def time: Double
	
	def fps: Double
}
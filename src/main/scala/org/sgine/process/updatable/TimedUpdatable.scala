package org.sgine.process.updatable

import scala.math._

/**
 * TimedUpdatable executes at a specific rate. The rate is defined as
 * the distance from completion of the previous update to the start of
 * the next update.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait TimedUpdatable extends Updatable {
	@volatile private var started = false
	
	protected def rate: Double
	
	/**
	 * Should be invoked to start this TimedUpdatable.
	 */
	def start(): Unit = if (!started) {
		started = true
		ready = false
	}
	
	// Overriding internal isReady to use timing
	override protected def ready = estimatedReady() == 0.0 || super.ready
	
	override protected def estimatedReady() = max(rate - elapsed, 0.0)
}
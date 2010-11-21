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
	def rate: Double
	
	/**
	 * Should be invoked to start this TimedUpdatable.
	 */
	def start(): Unit = ready = false
	
	// Overriding internal isReady to use timing
	override protected def isReady = estimatedReady() == 0.0 || super.isReady()
	
	override protected def estimatedReady() = max(rate - lastUpdated, 0.0)
}
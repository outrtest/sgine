package org.sgine.process

import org.sgine.bus._

import scala.reflect.Manifest

/**
 * Processor represents a monitoring object
 * capable of accepting incoming work from
 * Process.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Processor extends ObjectNode[() => Any] {
	implicit val manifest = Manifest.classType[() => Any](classOf[() => Any])
	val priority = ObjectNode.NormalPriority
	
	def apply(message: () => Any) = {
		if (accept(message)) {
			Routing.Stop
		} else {
			Routing.Continue
		}
	}
	
	/**
	 * This method will receive functions from
	 * Process. If this Processor is capable
	 * of handling the invocation immediately
	 * the method should return true immediately
	 * and asynchronously process the function.
	 * 
	 * @param f
	 * @return
	 * 		true if able to process immediately
	 */
	def accept(f: () => Any): Boolean
}
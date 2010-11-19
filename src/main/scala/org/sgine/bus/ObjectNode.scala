package org.sgine.bus

import scala.reflect.Manifest

trait ObjectNode[T] {
	def priority: Double
	implicit val manifest: Manifest[T]
	
	protected[bus] def receive[O](message: O)(implicit manifest: Manifest[O]) = {
		if (this.manifest.erasure.isAssignableFrom(manifest.erasure)) {
			apply(message.asInstanceOf[T])
		} else {
			Routing.Continue
		}
	}
	
	protected[bus] def apply(message: T): Routing
}

class FunctionalObjectNode[T] protected(val f: Function1[T, Routing], val priority: Double)(implicit val manifest: Manifest[T]) extends ObjectNode[T] {
	protected[bus] def apply(message: T) = f(message)
}

object ObjectNode {
	val HighestPriority = Double.MaxValue
	val HighPriority = 10.0
	val NormalPriority = 1.0
	val LowPriority = 0.5
	val LowestPriority = 0.0
	
	def apply[T](f: Function1[T, Routing], priority: Double = NormalPriority)(implicit manifest: Manifest[T]) = new FunctionalObjectNode[T](f, priority)
}
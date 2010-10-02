package org.sgine.bus

import scala.reflect.Manifest

class ObjectNode[T] protected(val f: Function1[T, Routing], val priority: Double)(implicit manifest: Manifest[T]) {
	def apply[O](message: O)(implicit manifest: Manifest[O]) = {
		if (this.manifest.erasure.isAssignableFrom(manifest.erasure)) {
			f(message.asInstanceOf[T])
		} else {
			Routing.Continue
		}
	}
}

object ObjectNode {
	val HighestPriority = Double.MaxValue
	val HighPriority = 10.0
	val NormalPriority = 1.0
	val LowPriority = 0.5
	val LowestPriority = 0.0
	
	def apply[T](f: Function1[T, Routing], priority: Double = NormalPriority)(implicit manifest: Manifest[T]) = new ObjectNode[T](f, priority)
}
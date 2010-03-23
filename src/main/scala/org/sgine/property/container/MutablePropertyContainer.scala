package org.sgine.property.container

import org.sgine.event._
import org.sgine.property._

trait MutablePropertyContainer extends PropertyContainer {
	override def +=(p: Property[_]): PropertyContainer = super.+=(p)
	
	override def -=(p: Property[_]): PropertyContainer = super.-=(p)
}

object MutablePropertyContainer {
	def apply(parent: Listenable) = new MutablePropertyContainerImplementation(parent)
}

class MutablePropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer
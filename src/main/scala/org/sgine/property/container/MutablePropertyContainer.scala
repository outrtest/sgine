package org.sgine.property.container

import org.sgine.event._

import org.sgine.property._

import scala.reflect.Manifest

trait MutablePropertyContainer extends PropertyContainer {
	override def addProperty(p: Property[_]): PropertyContainer = super.addProperty(p)
	
	override def removeProperty(p: Property[_]): PropertyContainer = super.removeProperty(p)
}

object MutablePropertyContainer {
	def apply(parent: Listenable) = new MutablePropertyContainerImplementation(parent)
}

class MutablePropertyContainerImplementation(override val parent: Listenable) extends PropertyContainer
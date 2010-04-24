package org.sgine.property

trait NamedProperty {
	def name: String
	
	override def toString() = name
}
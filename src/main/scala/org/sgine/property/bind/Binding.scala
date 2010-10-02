package org.sgine.property.bind

trait Binding[T] {
	def :=(value: T): Unit
}
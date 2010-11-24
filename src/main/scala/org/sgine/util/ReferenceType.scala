package org.sgine.util

import org.sgine.core.Enum
import org.sgine.core.Enumerated

sealed trait ReferenceType extends Enum {
	def create[T](value: T): Reference[T]
}

object ReferenceType extends Enumerated[ReferenceType] {
	case object Weak extends ReferenceType {
		def create[T](value: T) = Reference.weak(value)
	}
	case object Hard extends ReferenceType {
		def create[T](value: T) = Reference.hard(value)
	}
	
	ReferenceType(Weak, Hard)
}
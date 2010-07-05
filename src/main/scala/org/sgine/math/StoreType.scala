package org.sgine.math

import org.sgine.core.Enumerated
import org.sgine.core.Enumeration

import org.sgine.math.store._

sealed trait StoreType extends Enumeration {
	def create(length: Int): NumericStore
}

object StoreType extends Enumerated[StoreType] {
	case object BasicArray extends StoreType {
		def create(length: Int) = ArrayStore(length)
	}
	case object Buffer extends StoreType {
		def create(length: Int) = BufferStore(length, false)
	}
	case object DirectBuffer extends StoreType {
		def create(length: Int) = BufferStore(length, true)
	}
	
	var Default = BasicArray
	
	StoreType(BasicArray)
}
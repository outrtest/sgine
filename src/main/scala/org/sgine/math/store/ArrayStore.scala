package org.sgine.math.store

class ArrayStore private(val length: Int) extends NumericStore {
	private val array = new Array[Double](length)
	
	protected[math] def get(index: Int) = array(index)
	
	protected[math] def set(index: Int, value: Double) = {
		array(index) = value
		changed()
	}
	
	def copy() = {
		val as = new ArrayStore(length)
		as.copyFrom(this)
		as
	}
}

object ArrayStore {
	def apply(length: Int) = new ArrayStore(length)
}
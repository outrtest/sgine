package org.sgine.math.store

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.DoubleBuffer

class BufferStore private(val length: Int, direct: Boolean) extends NumericStore {
	val buffer = if (direct) ByteBuffer.allocateDirect(length * 8).order(ByteOrder.nativeOrder).asDoubleBuffer else DoubleBuffer.allocate(length)
	
	protected[math] def get(index: Int) = buffer.get(index)
	
	protected[math] def set(index: Int, value: Double) = {
		buffer.put(index, value)
		changed()
	}
	
	def copy() = {
		val bs = new BufferStore(length, direct)
		bs.copyFrom(this)
		bs
	}
}

object BufferStore {
	def apply(length: Int, direct: Boolean) = new BufferStore(length, direct)
}
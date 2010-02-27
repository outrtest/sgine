package org.sgine.math

import java.nio._

class VertexBuffer(val depth: Int, var buffer: FloatBuffer) {
	def set(index: Int, vertices: Float*): Unit = {
		for (depth <- 0 until vertices.length) {
			set(depth, index, vertices(depth))
		}
	}
	
	def set(depth: Int, index: Int, vertex: Float) = buffer.put((index * this.depth) + depth, vertex)
	
	def set(vertices: Float*): Unit = {
		for (index <- 0 until vertices.length) {
			buffer.put(index, vertices(index))
		}
	}
	
	def apply(depth: Int, index: Int) = buffer.get((index * this.depth) + depth)
	
	def apply(index: Int, f: (Float, Float, Float) => Unit): Unit = f(apply(0, index), apply(1, index), apply(2, index))
}

object VertexBuffer {
	def apply(depth: Int, vertices: Float*): VertexBuffer = {
		val length = vertices.length / depth
		val vb = apply(depth, length)
		vb.set(vertices: _*)
		vb
	}
	
	def apply(depth: Int, length: Int): VertexBuffer = new VertexBuffer(depth, ByteBuffer.allocateDirect((depth * length) * 4).order(ByteOrder.nativeOrder).asFloatBuffer())
}
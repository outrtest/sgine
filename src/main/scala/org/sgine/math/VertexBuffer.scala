package org.sgine.math

import java.nio._

class VertexBuffer private (val depth: Int, val length: Int, var buffer: DoubleBuffer) {
	def set(index: Int, vertices: Double*): Unit = {
		for (depth <- 0 until vertices.length) {
			set(depth, index, vertices(depth))
		}
	}
	
	def set(depth: Int, index: Int, vertex: Double) = buffer.put((index * this.depth) + depth, vertex)
	
	def set(vertices: Double*): Unit = {
		for (index <- 0 until vertices.length) {
			buffer.put(index, vertices(index))
		}
	}
	
	def apply(depth: Int, index: Int) = buffer.get((index * this.depth) + depth)
	
	def apply(index: Int, f: (Double, Double, Double) => Unit): Unit = f(apply(0, index), apply(1, index), apply(2, index))
	
	def apply(index: Int, f: (Double, Double) => Unit): Unit = f(apply(0, index), apply(1, index))
	
	def update(index: Int, v: Vector2) = {
		set(0, index, v.x)
		set(1, index, v.y)
	}
	
	def update(index: Int, v: Vector3) = {
		set(0, index, v.x)
		set(1, index, v.y)
		set(2, index, v.z)
	}
	
	def clone(newLength: Int): VertexBuffer = {
		val vb = VertexBuffer(depth, newLength)
		for (index <- 0 until buffer.capacity) {
			vb.buffer.put(index, buffer.get(index))
		}
		
		vb
	}
}

object VertexBuffer {
	def apply(depth: Int, vertices: Double*): VertexBuffer = {
		val length = vertices.length / depth
		val vb = apply(depth, length)
		vb.set(vertices: _*)
		vb
	}
	
	def apply(depth: Int, length: Int): VertexBuffer = new VertexBuffer(depth, length, ByteBuffer.allocateDirect((depth * length) * 8).order(ByteOrder.nativeOrder).asDoubleBuffer())
}
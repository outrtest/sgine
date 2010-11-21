package org.sgine.process.updatable

class FunctionUpdatable(f: (Double) => Unit) {
	def this(f: () => Unit) = this((d: Double) => f())
	
	def update(time: Double) = f(time)
}
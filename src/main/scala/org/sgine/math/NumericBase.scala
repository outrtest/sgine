package org.sgine.math

import org.sgine.math.store.NumericStore

trait NumericBase {
	protected[math] def store: NumericStore
	def mutability: Mutability
	
	def apply(index: Int) = store.get(index)
	
	protected def instance = if (mutability == Mutability.Mutable) this else copy()
	
	def update(index: Int, value: Double) = {
		val n = instance
		n.store.set(index, value)
		n
	}
	
	def length = store.length
	
	def copy(): NumericBase
	
	override def toString() = {
		val b = new StringBuilder()
		
		b.append(getClass.getSimpleName)
		b.append('(')
		for (index <- 0 until length) {
			if (index > 0) {
				b.append(", ")
			}
			b.append(this(index))
		}
		b.append(')')
		
		b.toString
	}
}
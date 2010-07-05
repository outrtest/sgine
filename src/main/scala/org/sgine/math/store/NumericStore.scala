package org.sgine.math.store

trait NumericStore {
	def length: Int
	var changeDelegate: () => Unit = _

	protected[math] def get(index: Int): Double
	
	protected[math] def set(index: Int, value: Double): Unit
	
	def copyFrom(store: NumericStore, offset: Int = 0) = {
		for (index <- 0 until length) {
			set(index, store.get(index + offset))
		}
	}
	
	def copy(): NumericStore
	
	def changed() = {
		if (changeDelegate != null) {
			changeDelegate()
		}
	}
}
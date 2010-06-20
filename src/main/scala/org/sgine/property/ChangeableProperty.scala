package org.sgine.property

/**
 * ChangeableProperty trait provides a simple abstract method
 * <code>changed(oldValue:T, newValue:T)</code> to allow notification
 * upon modification of the internal value.
 * 
 * @author Matt Hicks
 */
trait ChangeableProperty[T] extends Property[T] {
	private var oldValue: T = _
	
	abstract override def apply(value: T): Property[T] = {
		apply(value, true)
	}
	
	def apply(value: T, invokeListeners: Boolean): Property[T] = {
		val p = super.apply(value)
		
		if (invokeListeners) {
			changed()
		}
		
		p
	}
	
	def changed(equalityCheck: Boolean = true): Unit = {
		val newValue = apply()
		
		if ((!equalityCheck) || (newValue != oldValue)) {
			changed(oldValue, newValue)
		}
		
		oldValue = newValue
	}
	
	def changed(oldValue: T, newValue: T): Unit = {
	}
}

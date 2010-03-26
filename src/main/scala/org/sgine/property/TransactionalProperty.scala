package org.sgine.property

import org.sgine.event._
import event._

trait TransactionalProperty[T] extends ListenableProperty[T] {
	private var firstChange = true
	private var _originalValue: T = _
	private var _uncommitted = false
	
	abstract override def apply(value: T): Property[T] = {
		val p = super.apply(value)
		
		// First call should come across before setting uncommitted status
		if (firstChange) {
			firstChange = false
			_originalValue = value
		} else {	// Set uncommitted state
			_uncommitted = originalValue != value
		}
		
		p
	}
	
	def originalValue = _originalValue
	
	def uncommitted = _uncommitted
	
	def commit() = {
		if (uncommitted) {
			val e = PropertyTransactionEvent(this, _originalValue, apply(), true)
			
			_originalValue = apply()
			_uncommitted = false
			
			Event.enqueue(e)
			
			true
		} else {
			false
		}
	}
	
	def revert() = {
		if (uncommitted) {
			val e = PropertyTransactionEvent(this, apply(), _originalValue, false)
			
			super.apply(_originalValue)
			_uncommitted = false
			
			Event.enqueue(e)
			
			true
		} else {
			false
		}
	}
}
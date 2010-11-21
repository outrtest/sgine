package org.sgine.property

import org.sgine.event._

import org.sgine.transaction._

trait TransactionalProperty[T] extends ListenableProperty[T] with ChangeableProperty[T] with Transactable[T] {
	abstract override def apply() = transactionGet() match {
		case Some(v) => v
		case None => super.apply()
	}
	
	abstract override def apply(value: T): Property[T] = {
		if (!transactionSet(value)) {
			super.apply(value)
		}
		this
	}
	
	def commit() = transactionGet() match {
		case Some(value) => super.apply(value); revert(); true
		case None => false
	}
	
	def originalValue = super.apply()
	
	def revert() = transactionRevert()
	
	protected def transactionStarted() = {
	}
	
	protected def transactionCommit(value: T) = {
		apply(value, false)		// Apply the value to the Property without invoking listeners
	}
	
	protected def transactionRollback(value: T) = {
	}
	
	protected def transactionFinished() = {
		changed()				// Invoke listeners
	}
}
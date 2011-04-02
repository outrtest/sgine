package org.sgine.transaction

/**
 * Transactable provides support for integration within
 * Transactions.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Transactable[T] {
	/**
	 * Should be called when the value of this Transactable
	 * is changed.
	 * 
	 * @param
	 * 		The new value to be set to this transaction
	 * @return
	 * 		true if transaction is currently running
	 */
	protected def transactionSet(value: T) = Transaction.set(this, value)
	
	/**
	 * Should be called when the value of this Transactable
	 * is being retrieved. If a transaction exists on this
	 * thread the current transactional value will be returned
	 * else None.
	 * 
	 * @return
	 * 		Option[T]
	 */
	protected def transactionGet() = Transaction.get(this).asInstanceOf[Option[T]]
	
	protected def transactionRevert() = Transaction.revert(this)
	
	/**
	 * Called at the beginning of a transaction commit.
	 */
	protected def transactionStarted(): Unit
	
	/**
	 * Called to commit this transactable.
	 */
	protected def transactionCommit(value: T): Unit
	
	/**
	 * Called when rolling back a transaction.
	 */
	protected def transactionRollback(value: T): Unit
	
	/**
	 * Called at the end end of a transaction commit.
	 */
	protected def transactionFinished(): Unit
	
	def uncommitted = transactionGet() != None
	
	private def transactionCommitConversion(value: Any) = transactionCommit(value.asInstanceOf[T])
	private def transactionRollbackConversion(value: Any) = transactionRollback(value.asInstanceOf[T])
}

object Transactable {
	val started = ((t: Transactable[_], v: Any) => t.transactionStarted()).tupled
	val commit = ((t: Transactable[_], v: Any) => t.transactionCommitConversion(v)).tupled
	val rollback = ((t: Transactable[_], v: Any) => t.transactionRollbackConversion(v)).tupled
	val finished = ((t: Transactable[_], v: Any) => t.transactionFinished()).tupled
}
package org.sgine.transaction

/**
 * Transactable provides support for integration within
 * Transactions.
 * 
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Transactable {
	/**
	 * Should be called when the value of this Transactable
	 * is changed.
	 * 
	 * @return
	 * 		true if transaction is currently running
	 */
	protected[transaction] def transactionValueChanged() = Transaction.changed(this)
	
	/**
	 * Called at the beginning of a transaction commit.
	 */
	protected[transaction] def transactionStarted(): Unit
	
	/**
	 * Called to commit this transactable.
	 */
	protected[transaction] def transactionCommit(): Unit
	
	/**
	 * Called when rolling back a transaction.
	 */
	protected[transaction] def transactionRollback(): Unit
	
	/**
	 * Called at the end end of a transaction commit.
	 */
	protected[transaction] def transactionFinished(): Unit
}
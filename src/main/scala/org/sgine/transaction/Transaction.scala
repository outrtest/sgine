package org.sgine.transaction

object Transaction {
	private val current = new ThreadLocal[Transaction]
	
	private def initiate() = {
		if (!running) {
			current.set(new Transaction)
		} else {
			throw new RuntimeException("Transaction already running!")
		}
	}
	
	def apply(f: => Unit) = {
		initiate()
		try {
			f
			
			current.get.commit()
		} catch {
			case exc => {
				current.get.rollback()
				throw exc
			}
		} finally {
			current.set(null)
		}
	}
	
	def running = current.get != null
	
	protected[transaction] def changed(transactable: Transactable) = current.get match {
		case null => false
		case t => t.changed(transactable)
	}
}

private class Transaction {
	private var transactables: List[Transactable] = Nil
	
	private val transactionStarted = (transactable: Transactable) => transactable.transactionStarted()
	private val transactionCommit = (transactable: Transactable) => transactable.transactionCommit()
	private val transactionRollback = (transactable: Transactable) => transactable.transactionRollback()
	private val transactionFinished = (transactable: Transactable) => transactable.transactionFinished()
	
	def changed(transactable: Transactable) = transactables = transactable :: transactables
	
	def commit() = {
		transactables.foreach(transactionStarted)
		transactables.foreach(transactionRollback)
		transactables.foreach(transactionFinished)
		
		transactables = Nil
	}
	
	def rollback() = {
		transactables.foreach(transactionRollback)
		
		transactables = Nil
	}
}
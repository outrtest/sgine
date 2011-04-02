package org.sgine.transaction

object Transaction {
	private val current = new ThreadLocal[Transaction]
	
	private def initiate() = {
		if (!running) {
			val t = new Transaction
			current.set(t)
			t
		} else {
			throw new RuntimeException("Transaction already running!")
		}
	}
	
	def apply(f: => Unit) = {
		val t = initiate()
		try {
			f
			
			t.commit()
		} catch {
			case exc => {
				t.rollback()
				throw exc
			}
		} finally {
			current.set(null)
		}
	}
	
	def running = current.get != null
	
	protected[transaction] def set(transactable: Transactable[_], value: Any) = current.get match {
		case null => false
		case t => t.set(transactable, value); true
	}
	
	protected[transaction] def get(transactable: Transactable[_]) = current.get match {
		case null => None
		case t => t.get(transactable)
	}
	
	protected[transaction] def revert(transactable: Transactable[_]) = current.get match {
		case null => false
		case t => t.revert(transactable); true
	}
	
	/**
	 * True if there is an uncommitted value for this Transactable in the
	 * current thread.
	 * 
	 * @param transactable
	 * @return
	 * 		true if uncommitted
	 */
	def transaction(transactable: Transactable[_]) = current.get match {
		case null => false
		case t => t.map.contains(transactable)
	}
}

private class Transaction {
	private var map = Map.empty[Transactable[_], Any]
	
	def get(transactable: Transactable[_]) = map.get(transactable)
	
	def set(transactable: Transactable[_], value: Any) = map += transactable -> value
	
	def revert(transactable: Transactable[_]) = map -= transactable
	
	def commit() = {
		map.foreach(Transactable.started)
		map.foreach(Transactable.commit)
		map.foreach(Transactable.finished)
		
		map = Map.empty
	}
	
	def rollback() = {
		map.foreach(Transactable.rollback)
		
		map = Map.empty
	}
}
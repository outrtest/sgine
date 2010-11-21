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
	
	protected[transaction] def set(transactable: Transactable[_], value: Any) = current.get match {
		case null => false
		case t => t.set(transactable, value); true
	}
	
	protected[transaction] def get(transactable: Transactable[_]) = current.get match {
		case null => None
		case t => t.get(transactable)
	}
}

private class Transaction {
	private var map = Map.empty[Transactable[_], Any]
	
	def get(transactable: Transactable[_]) = map.get(transactable)
	
	def set(transactable: Transactable[_], value: Any) = map += transactable -> value
	
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
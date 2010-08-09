package org.sgine.db

class AutoCommitTransaction(transaction: Transaction) extends Transaction {
	def db = transaction.db
	
	def store(obj: AnyRef) = {
		transaction.store(obj)
		transaction.commit()
	}
	
	def query[T](clazz: Class[T]) = transaction.query(clazz)
	
	def query[T](predicate: T => Boolean) = transaction.query(predicate)
	
	def delete(obj: AnyRef) = {
		transaction.delete(obj)
		transaction.commit()
	}
	
	def commit() = transaction.commit()
	
	def rollback() = transaction.rollback()
	
	def close() = transaction.close()
}
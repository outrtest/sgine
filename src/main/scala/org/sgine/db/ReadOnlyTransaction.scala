package org.sgine.db

class ReadOnlyTransaction(transaction: Transaction) extends ReadTransaction {
	def db = transaction.db
	
	def query[T](clazz: Class[T]) = transaction.query(clazz)
	
	def query[T](predicate: T => Boolean) = transaction.query(predicate)
	
	def close() = transaction.close()
}
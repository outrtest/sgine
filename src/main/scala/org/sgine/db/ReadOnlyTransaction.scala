package org.sgine.db

import scala.reflect.Manifest

class ReadOnlyTransaction(transaction: Transaction) extends ReadTransaction {
	def db = transaction.db
	
	def query[T](clazz: Class[T]) = transaction.query(clazz)
	
	def query[T](predicate: T => Boolean, sortFunction: (T, T) => Int = null)(implicit manifest: Manifest[T]) = transaction.query(predicate, sortFunction)
	
	def close() = transaction.close()
}
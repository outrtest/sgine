package org.sgine.db

import scala.reflect.Manifest

class AutoCommitTransaction(transaction: Transaction) extends Transaction {
	def db = transaction.db
	
	def store[T](obj: T)(implicit manifest: Manifest[T]) = {
		transaction.store(obj)
		transaction.commit()
	}
	
	def query[T](clazz: Class[T]) = transaction.query(clazz)
	
	def query[T](predicate: T => Boolean, sortFunction: (T, T) => Int = null)(implicit manifest: Manifest[T]) = transaction.query(predicate, sortFunction)
	
	def delete[T](obj: T)(implicit manifest: Manifest[T]) = {
		transaction.delete(obj)
		transaction.commit()
	}
	
	def commit() = transaction.commit()
	
	def rollback() = transaction.rollback()
	
	def close() = transaction.close()
}
package org.sgine.db

trait Transaction {
	def store(obj: AnyRef): Unit
	
	def query[T](clazz: Class[T]): Iterator[T]
	
	def query[T](predicate: T => Boolean): Iterator[T]
	
	def find[T](predicate: T => Boolean) = {
		val i = query(predicate)
		if (i.hasNext) {
			Some(i.next)
		} else {
			None
		}
	}
	
	def find[T](clazz: Class[T]) = {
		val i = query(clazz)
		if (i.hasNext) {
			Some(i.next)
		} else {
			None
		}
	}
	
	def delete(obj: AnyRef): Unit
	
	def commit(): Unit
	
	def rollback(): Unit
	
	def close(): Unit
}
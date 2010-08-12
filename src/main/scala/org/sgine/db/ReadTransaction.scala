package org.sgine.db

trait ReadTransaction {
	def db: DB
	
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
	
	def close(): Unit
}
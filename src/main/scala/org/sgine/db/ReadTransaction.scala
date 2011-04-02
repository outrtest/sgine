package org.sgine.db

import scala.reflect.Manifest

trait ReadTransaction {
	def db: DB
	
	def query[T](predicate: T => Boolean = null, sortFunction: (T, T) => Int = null)(implicit manifest: Manifest[T]): Iterator[T]
	
	def find[T](predicate: T => Boolean = null, sortFunction: (T, T) => Int = null)(implicit manifest: Manifest[T]) = {
		val i = query[T](predicate, sortFunction)
		if (i.hasNext) {
			Some(i.next)
		} else {
			None
		}
	}
	
	def close(): Unit
}
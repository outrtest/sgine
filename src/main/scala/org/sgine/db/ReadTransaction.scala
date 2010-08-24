package org.sgine.db

import scala.reflect.Manifest

trait ReadTransaction {
	def db: DB
	
	def query[T](clazz: Class[T]): Iterator[T]
	
	def query[T](predicate: T => Boolean)(implicit manifest: Manifest[T]): Iterator[T]
	
	def find[T](predicate: T => Boolean)(implicit manifest: Manifest[T]) = {
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
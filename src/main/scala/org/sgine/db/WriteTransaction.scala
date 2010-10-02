package org.sgine.db

import scala.reflect.Manifest

trait WriteTransaction {
	def db: DB
	
	def store[T](obj: T)(implicit manifest: Manifest[T]): Unit
	
	def delete[T](obj: T)(implicit manifest: Manifest[T]): Unit
	
	def commit(): Unit
	
	def rollback(): Unit
	
	def close(): Unit
}
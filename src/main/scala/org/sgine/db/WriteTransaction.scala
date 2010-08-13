package org.sgine.db

trait WriteTransaction {
	def db: DB
	
	def store(obj: AnyRef): Unit
	
	def delete(obj: AnyRef): Unit
	
	def commit(): Unit
	
	def rollback(): Unit
	
	def close(): Unit
}
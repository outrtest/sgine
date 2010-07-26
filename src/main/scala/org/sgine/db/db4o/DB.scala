package org.sgine.db.db4o

import com.db4o.ObjectContainer
import com.db4o.query.Predicate

class DB(container: ObjectContainer) extends org.sgine.db.DB {
	def store(obj: AnyRef) = container.store(obj)
	
	def query[T](predicate: T => Boolean) = new RichObjectSet(container.query(new Predicate[T]() {
		def `match`(entry: T) = predicate(entry)
	}))
	
	def delete(obj: AnyRef) = container.delete(obj)
	
	def commit() = container.commit()
	
	def rollback() = container.rollback()
	
	protected def close() = container.close()
}
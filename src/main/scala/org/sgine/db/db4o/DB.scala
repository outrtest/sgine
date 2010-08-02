package org.sgine.db.db4o

import com.db4o.Db4oEmbedded
import com.db4o.ObjectContainer
import com.db4o.ObjectSet
import com.db4o.query.Predicate

import java.io.File

import org.sgine.db.DBFactory

class DB(container: ObjectContainer, val autoCommit: Boolean) extends org.sgine.db.DB {
	def store(obj: AnyRef) = {
		container.store(obj)
		if (autoCommit) commit()
	}
	
	def query[T](clazz: Class[T]) = new RichObjectSet(container.query(clazz))
	
	def query[T](predicate: T => Boolean) = new RichObjectSet(container.query(new Predicate[T]() {
		def `match`(entry: T) = predicate(entry)
	}))
	
	def delete(obj: AnyRef) = {
		container.delete(obj)
		if (autoCommit) commit()
	}
	
	def commit() = container.commit()
	
	def rollback() = container.rollback()
	
	protected def close() = container.close()
}

class RichObjectSet[T](objectSet: ObjectSet[T]) extends Iterator[T] {
	def hasNext = objectSet.hasNext
	def next = objectSet.next
}

object DB extends DBFactory {
	def apply(file: File, autoCommit: Boolean) = new DB(Db4oEmbedded.openFile(file.getAbsolutePath), autoCommit)
}
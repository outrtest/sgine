package org.sgine.db.db4o

import com.db4o.ObjectContainer
import com.db4o.ObjectServer
import com.db4o.ObjectSet
import com.db4o.cs.Db4oClientServer
import com.db4o.query.Predicate

import java.io.File

import org.sgine.db.DBFactory

class DB(server: ObjectServer) extends org.sgine.db.DB {
	protected def createTransaction() = new Transaction(server.openClient())
	
	def close() = server.close()
}

class Transaction(container: ObjectContainer) extends org.sgine.db.Transaction {
	def store(obj: AnyRef) = container.store(obj)
	
	def query[T](clazz: Class[T]) = new RichObjectSet(container.query(clazz))
	
	def query[T](predicate: T => Boolean) = new RichObjectSet(container.query(new Predicate[T]() {
		def `match`(entry: T) = predicate(entry)
	}))
	
	def delete(obj: AnyRef) = container.delete(obj)
	
	def commit() = container.commit()
	
	def rollback() = container.rollback()
	
	def close() = container.close()
}

class RichObjectSet[T](objectSet: ObjectSet[T]) extends Iterator[T] {
	def hasNext = objectSet.hasNext
	def next = objectSet.next
}

object DB extends DBFactory {
	def apply(file: File) = new DB(Db4oClientServer.openServer(file.getAbsolutePath, 0))
}
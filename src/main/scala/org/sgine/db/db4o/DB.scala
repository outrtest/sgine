package org.sgine.db.db4o

import com.db4o.Db4o
import com.db4o.ObjectContainer
import com.db4o.ObjectServer
import com.db4o.ObjectSet
import com.db4o.cs.Db4oClientServer
import com.db4o.query.Predicate

import java.io.File

import org.sgine.db.DBFactory

import scala.reflect.Manifest

class DB(server: ObjectServer) extends org.sgine.db.DB {
	protected def createTransaction() = new Transaction(this, server.openClient())
	
	def close() = server.close()
}

class Transaction(val db: DB, container: ObjectContainer) extends org.sgine.db.Transaction {
	def store[T](obj: T)(implicit manifest: Manifest[T]) = container.store(obj)
	
	def query[T](clazz: Class[T]) = new RichObjectSet(container.query(clazz))
	
	def query[T](predicate: T => Boolean)(implicit manifest: Manifest[T]) = new RichObjectSet(container.query(new Predicate[T](manifest.erasure.asInstanceOf[Class[T]]) {
		def `match`(entry: T) = predicate(entry)
	}))
	
	def delete[T](obj: T)(implicit manifest: Manifest[T]) = container.delete(obj)
	
	def commit() = container.commit()
	
	def rollback() = container.rollback()
	
	def close() = container.close()
}

class RichObjectSet[T](objectSet: ObjectSet[T]) extends Iterator[T] {
	def hasNext = objectSet.hasNext
	def next = objectSet.next
}

object DB extends DBFactory {
	def apply(file: File) = {
		val config = Db4oClientServer.newServerConfiguration
		val path = file match {
			case null => {		// Set to in-memory storage if file is null
				config.file().storage(new com.db4o.io.PagingMemoryStorage())
				null
			}
			case f => f.getAbsolutePath
		}
		new DB(Db4oClientServer.openServer(config, path, 0))
	}
}
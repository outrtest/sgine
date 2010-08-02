package org.sgine.db.neodatis

import java.io.File

import org.neodatis.odb.Objects
import org.neodatis.odb.ODB
import org.neodatis.odb.ODBFactory
import org.neodatis.odb.core.query.nq.NativeQuery

import org.sgine.db.DBFactory

import scala.collection.JavaConversions._

class DB(container: ODB, val autoCommit: Boolean) extends org.sgine.db.DB {
	def store(obj: AnyRef) = {
		container.store(obj)
		if (autoCommit) commit()
	}
	
	def query[T](clazz: Class[T]) = new RichObjects(container.getObjects(clazz))
	
	def query[T](predicate: T => Boolean) = new RichObjects(container.getObjects(new ObjectQuery(predicate)))
	
	def delete(obj: AnyRef) = {
		container.delete(obj)
		if (autoCommit) commit()
	}
	
	def commit() = container.commit()
	
	def rollback() = container.rollback()
	
	protected def close() = container.close()
}

class ObjectQuery[T](predicate: T => Boolean) extends NativeQuery {
	override def `match`(entry: AnyRef) = {
		println("MATCH: " + entry)
		predicate(entry.asInstanceOf[T])
	}
	
	def getObjectType = classOf[AnyRef]
}

class RichObjects[T](objects: Objects[T]) extends Iterator[T] {
	def hasNext = objects.hasNext
	def next = objects.next
}

object DB extends DBFactory {
	def apply(file: File, autoCommit: Boolean) = new DB(ODBFactory.open(file.getAbsolutePath()), autoCommit)
}
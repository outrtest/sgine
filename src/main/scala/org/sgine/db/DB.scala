package org.sgine.db

import java.io.File

trait DB {
	def autoCommit: Boolean
	
	def store(obj: AnyRef): Unit
	
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
	
	def delete(obj: AnyRef): Unit
	
	def commit(): Unit
	
	def rollback(): Unit
	
	protected def close(): Unit
}

object DB {
	// TODO: fix neodatis support and make default - neodatis doesn't support extended classes
	var Factory = org.sgine.db.db4o.DB
	
	private var map = Map.empty[String, DB]
	
	def apply(name: String) = map(name)
	
	def open(name: String, file: File, autoCommit: Boolean = false) = {
		synchronized {
			val db = map.get(name) match {
				case Some(db) => {
					if (db.autoCommit != autoCommit) throw new RuntimeException("Connection already exists and AutoCommit is " + db.autoCommit)
					db
				}
				case None => {
					val db = Factory(file, autoCommit)
					map += name -> db
					db
				}
			}
			
			db
		}
	}
	
	def close(name: String) = {
		synchronized {
			val db = map(name)
			map -= name
			db.close()
		}
	}
}
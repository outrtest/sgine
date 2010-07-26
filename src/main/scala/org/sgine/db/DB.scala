package org.sgine.db

import java.io.File

trait DB {
	def autoCommit: Boolean
	
	def store(obj: AnyRef): Unit
	
	def query[T](predicate: T => Boolean): Iterator[T]
	
	def delete(obj: AnyRef): Unit
	
	def commit(): Unit
	
	def rollback(): Unit
	
	protected def close(): Unit
}

object DB {
	var Factory: DBFactory = neodatis.DB
	
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
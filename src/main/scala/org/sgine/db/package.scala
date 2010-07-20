package org.sgine

import com.db4o.Db4o
import com.db4o.ObjectContainer
import com.db4o.ObjectSet
import com.db4o.query.Predicate

import java.io.File

package object db {
	private var databases = Map.empty[File, ObjectContainer]
	
	implicit def function2Predicate[T](predicate: T => Boolean) = {
		new Predicate[T]() {
			def `match`(entry: T) = predicate(entry)
		}
	}
	
	implicit def objectSet2RichObjectSet[T](objectSet: ObjectSet[T]) = new RichObjectSet[T](objectSet)
	
	def openDatabase(file: File) = {
		synchronized {
			val db = databases.get(file) match {
				case Some(d) => d
				case _ => Db4o.openFile(file.getAbsolutePath)
			}
			
			db
		}
	}
	
	def closeDatabase(file: File) = {
		synchronized {
			databases(file).close()
			databases -= file
		}
	}
}
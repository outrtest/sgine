package org.sgine.log

import java.io.File

import org.sgine.db._

class DB4OLogger(file: File) extends Logger {
	lazy val db = openDatabase(file)
	
	def log(log: Log) = db.store(log)
}
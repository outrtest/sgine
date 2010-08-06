package org.sgine.log

import java.io.File

import org.sgine.db._

class DB4OLogger(db: DB) extends Logger {
	lazy val transaction = db.transaction(true)
	
	def log(log: Log) = transaction.store(log)
}
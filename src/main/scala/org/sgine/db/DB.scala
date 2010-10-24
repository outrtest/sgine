package org.sgine.db

import java.io.File

trait DB {
	protected def createTransaction(): Transaction
	
	def transaction(autoCommit: Boolean = false) = if (autoCommit) new AutoCommitTransaction(createTransaction()) else createTransaction()
	
	def readOnlyTransaction(): ReadTransaction = new ReadOnlyTransaction(createTransaction())
	
	def close(): Unit
}

object DB {
	// TODO: fix neodatis support and make default - neodatis doesn't support extended classes
	var factory: DBFactory = org.sgine.db.db4o.DB
	
	def open(file: File) = {
		if (file.getParentFile != null) {	// Make sure the path exists
			file.getParentFile.mkdirs()
		}
		factory(file)
	}
	
//	def open(host: String, port: Int, username: String = null, password: String = null) = Factory.open(host, port, username, password)
}
package org.sgine.log

trait Logger {
	protected[log] def valid(log: Log) = true
	
	def log(log: Log): Unit
}
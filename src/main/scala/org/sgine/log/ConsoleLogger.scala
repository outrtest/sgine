package org.sgine.log

object ConsoleLogger extends Logger {
	def log(log: Log) = println(log)
}
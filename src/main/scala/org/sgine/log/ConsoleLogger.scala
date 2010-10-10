package org.sgine.log

object ConsoleLogger extends Logger {
	def log(log: Log) = {
		println(log)
		log.reference match {
			case null => // Do nothing
			case t: Throwable => t.printStackTrace()
			case o: Any => println("\t" + o)
		}
	}
}
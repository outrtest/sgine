package org.sgine.log

import java.io.File

import java.util.Calendar
import java.util.UUID

case class Log(
			   message: String,
			   messageType: String = null,
			   method: String = null,
			   className: String = null,
			   level: LogLevel = LogLevel.Info,
			   reference: AnyRef = null,
			   date: Calendar = Calendar.getInstance,
			   thread: String = Thread.currentThread.getName,
			   application: String = Log.application,
			   uuid: UUID = UUID.randomUUID()
		) {
	def send() = Log.log(this)
	
	override def toString() = {
		String.format(Log.dateFormat, date) +
		"\t" +
		message + " (" + level.name + " - " + level.value + ")"
	}
}
		
object Log {
	val All = 0
	val None = Int.MaxValue
	
	var dateFormat = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %1$tZ"
	var application: String = null
	var logger: Logger = ConsoleLogger
	var level: Int = LogLevel.Info.value
	
	private def log(log: Log) = {
		if (log.level.value >= level) {
			if (logger.valid(log)) {
				logger.log(log)
			}
		}
	}
	
	def main(args: Array[String]): Unit = {
		Log("testing").send()
	}
}
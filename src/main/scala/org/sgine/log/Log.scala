package org.sgine.log

import java.io.File

import java.util.Calendar
import java.util.UUID

class Log protected(
			   val message: String,
			   val messageType: String,
			   val method: String,
			   val className: String,
			   val level: LogLevel,
			   val reference: AnyRef,
			   val date: Calendar,
			   val thread: String,
			   val application: String,
			   val uuid: UUID
		) {
	protected def send(): Unit = Log.log(this)
	
	private val _fields = List(date _, application _, level _, thread _, messageType _, method _, className _, reference _, message _)
	protected def fields = _fields
	
	override def toString() = {
		val b = new StringBuilder()
		
		for (f <- fields) {
			val value = f() match {
				case null => null
				case c: Calendar => String.format(Log.dateFormat, c)
				case l: LogLevel => level.name
				case o => o
			}
			if (value != null) {
				if (b.length > 0) {
					b.append(' ')
				}
				b.append(value.toString)
			}
		}
		
		b.toString
	}
}
		
object Log {
	val All = 0
	val None = Int.MaxValue
	
	var dateFormat = "%1$tb %1$td %1$tH:%1$tM:%1$tS"
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
	
	// TODO: add support for args: AnyRef* to be applied via String.format(message, args)
	def apply( message: String,
			   messageType: String = "default",
			   method: String = null,
			   className: String = null,
			   level: LogLevel = LogLevel.Info,
			   reference: AnyRef = null,
			   date: Calendar = Calendar.getInstance,
			   thread: String = Thread.currentThread.getName,
			   application: String = Log.application,
			   uuid: UUID = UUID.randomUUID()) = {
		val l = new Log(message, messageType, method, className, level, reference, date, thread, application, uuid)
		l.send()
		l
	}
	
	def main(args: Array[String]): Unit = {
		Log("testing")
		WebLog("testing 2")
	}
}
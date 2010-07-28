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
	private var loggers: List[Logger] = Nil
	var level: Int = LogLevel.Info.value
	
	addLogger(ConsoleLogger)		// Configure default logger
	
	private def log(log: Log) = {
		for (logger <- loggers) {
			if (logger.valid(log)) {
				logger.log(log)
			}
		}
	}
	
	def addLogger(logger: Logger) = {
		synchronized {
			loggers = logger :: loggers
		}
	}
	
	def apply(message: String,
			  messageType: String = null,
			  method: String = null,
			  className: String = null,
			  level: LogLevel = LogLevel.Info,
			  reference: AnyRef = null,
			  args: Seq[AnyRef] = null) = {
		if (level.value >= this.level) {
			val m = args match {
				case null => message
				case _ => String.format(message, args: _*)
			}
			val l = new Log(m, messageType, method, className, level, reference, Calendar.getInstance, Thread.currentThread.getName, Log.application, UUID.randomUUID)
			l.send()
			l
		}
	}
	
	def test(message: String)(implicit date: Calendar = Calendar.getInstance) = {
	}
	
	def main(args: Array[String]): Unit = {
		Log("testing %1s / %2s, Today: %3$tA", args = List("First", "Second", Calendar.getInstance))
		
		WebLog("testing 2")
		test("one")()
	}
}
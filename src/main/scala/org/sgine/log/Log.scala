package org.sgine.log

import java.io.File

import java.util.Calendar
import java.util.UUID

class Log protected(
			   val message: String,
			   val messageType: String,
			   val className: String,
			   val method: String,
			   val lineNumber: String,
			   val level: LogLevel,
			   val reference: AnyRef,
			   val date: Calendar,
			   val thread: String,
			   val application: String,
			   val uuid: UUID
		) {
	protected def send(): Unit = Log.log(this)
	
	def source = if (className != null) {
		className + "." + method + "." + lineNumber
	} else {
		null
	}
	
	private val _fields = List(date _, application _, level _, thread _, messageType _, source _, reference _, message _)
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
	
	var dateFormat = "%1$tb %1$td %1$tH:%1$tM:%1$tS:%1$tL"
	var application: String = null
	private var loggers: List[Logger] = Nil
	var level: Int = LogLevel.Info.value
	/**
	 * sourceLookup determines whether logging should default to
	 * lookup the method and class of the caller by default if not
	 * specified.
	 * 
	 * Defaults to false.
	 */
	var sourceLookup = false
	
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
	
	def apply(message: Any,
			  messageType: String = null,
			  className: String = null,
			  method: String = null,
			  lineNumber: String = null,
			  level: LogLevel = LogLevel.Info,
			  reference: AnyRef = null,
			  args: Seq[Any] = null) = {
		if (level.value >= this.level) {
			val m = args match {
				case null => message.toString
				case _ => String.format(message.toString, args.asInstanceOf[Seq[AnyRef]]: _*)
			}
			var cn = className
			var mn = method
			var ln = lineNumber
			if ((mn == null) && (cn == null) && (sourceLookup)) {		// Do source lookups
				val stack = Thread.currentThread.getStackTrace
				mn = stack(2).getMethodName
				cn = stack(2).getClassName
				ln = stack(2).getLineNumber.toString
			}
			val l = new Log(m, messageType, cn, mn, ln, level, reference, Calendar.getInstance, Thread.currentThread.getName, Log.application, UUID.randomUUID)
			l.send()
			l
		}
	}
}
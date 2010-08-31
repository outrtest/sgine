package org.sgine.log

import java.util.Calendar
import java.util.UUID

class WebLog protected(
			   message: String,
			   messageType: String,
			   val user: String,
			   val host: String,
			   val session: String,
			   className: String,
			   method: String,
			   lineNumber: String,
			   level: LogLevel,
			   reference: AnyRef,
			   date: Calendar,
			   thread: String,
			   application: String,
			   uuid: UUID
		) extends Log(message, messageType, className, method, lineNumber, level, reference, date, thread, application, uuid) {
	
	override protected val fields = super.fields.head :: user _ :: host _ :: session _ :: super.fields.tail
}

object WebLog {
	def apply(message: String,
			   messageType: String = "web",
			   user: String = null,
			   host: String = null,
			   session: String = null,
			   className: String = null,
			   method: String = null,
			   lineNumber: String = null,
			   level: LogLevel = LogLevel.Info,
			   reference: AnyRef = null,
			   args: Seq[AnyRef] = null) = {
		if (level.value >= Log.level) {
			val m = args match {
				case null => message
				case _ => String.format(message, args: _*)
			}
			val l = new WebLog(m, messageType, user, host, session, className, method, lineNumber, level, reference, Calendar.getInstance, Thread.currentThread.getName, Log.application, UUID.randomUUID)
			l.send()
			l
		}
	}
}
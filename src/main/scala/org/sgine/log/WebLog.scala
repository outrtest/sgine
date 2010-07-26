package org.sgine.log

import java.util.Calendar
import java.util.UUID

class WebLog protected(
			   message: String,
			   messageType: String,
			   val user: String,
			   val host: String,
			   val session: String,
			   method: String,
			   className: String,
			   level: LogLevel,
			   reference: AnyRef,
			   date: Calendar,
			   thread: String,
			   application: String,
			   uuid: UUID
		) extends Log(message, messageType, method, className, level, reference, date, thread, application, uuid) {
	
	override protected val fields = super.fields.head :: user _ :: host _ :: session _ :: super.fields.tail
}

object WebLog {
	def apply(message: String,
			   messageType: String = "web",
			   user: String = null,
			   host: String = null,
			   session: String = null,
			   method: String = null,
			   className: String = null,
			   level: LogLevel = LogLevel.Info,
			   reference: AnyRef = null,
			   date: Calendar = Calendar.getInstance,
			   thread: String = Thread.currentThread.getName,
			   application: String = Log.application,
			   uuid: UUID = UUID.randomUUID()) = {
		val l = new WebLog(message, messageType, user, host, session, method, className, level, reference, date, thread, application, uuid)
		l.send()
		l
	}
}
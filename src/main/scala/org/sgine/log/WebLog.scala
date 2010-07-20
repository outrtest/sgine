package org.sgine.log

import java.util.Calendar
import java.util.UUID

case class WebLog(
			   override val message: String,
			   override val messageType: String = "web",
			   user: String = null,
			   host: String = null,
			   session: String = null,
			   override val method: String = null,
			   override val className: String = null,
			   override val level: LogLevel = LogLevel.Info,
			   override val reference: AnyRef = null,
			   override val date: Calendar = Calendar.getInstance,
			   override val thread: String = Thread.currentThread.getName,
			   override val application: String = Log.application,
			   override val uuid: UUID = UUID.randomUUID()
		) extends Log(message, messageType, method, className, level, reference, date, thread, application, uuid)
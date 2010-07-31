package org.sgine

package object log {
	val log = Log
	
	val info = Log
	
	def warn(message: Any,
			 messageType: String = null,
			 method: String = null,
			 className: String = null,
			 level: LogLevel = LogLevel.Warning,
			 reference: AnyRef = null,
			 args: Seq[AnyRef] = null) = Log(message, messageType, method, className, level, reference, args)
			 
	def trace(message: Any,
			  throwable: Throwable,
			  messageType: String = "stacktrace",
			  method: String = null,
			  className: String = null,
			  level: LogLevel = LogLevel.Warning,
			  args: Seq[AnyRef] = null) = Log(message, messageType, method, className, level, throwable, args)
	
	def debug(message: Any,
			  messageType: String = null,
			  method: String = null,
			  className: String = null,
			  level: LogLevel = LogLevel.Debug,
			  reference: AnyRef = null,
			  args: Seq[AnyRef] = null) = Log(message, messageType, method, className, level, reference, args)
			  
	val web = WebLog
}
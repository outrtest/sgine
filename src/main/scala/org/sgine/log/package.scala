package org.sgine

package object log {
	val log = Log
	
	def finest(message: Any,
			 messageType: String = null,
			 className: String = null,
			 method: String = null,
			 lineNumber: String = null,
			 level: LogLevel = LogLevel.Finest,
			 reference: AnyRef = null,
			 args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
			 
	def finer(message: Any,
			 messageType: String = null,
			 className: String = null,
			 method: String = null,
			 lineNumber: String = null,
			 level: LogLevel = LogLevel.Finer,
			 reference: AnyRef = null,
			 args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
			 
	def fine(message: Any,
			 messageType: String = null,
			 className: String = null,
			 method: String = null,
			 lineNumber: String = null,
			 level: LogLevel = LogLevel.Fine,
			 reference: AnyRef = null,
			 args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
			 
	def debug(message: Any,
			  messageType: String = null,
			  className: String = null,
			  method: String = null,
			  lineNumber: String = null,
			  level: LogLevel = LogLevel.Debug,
			  reference: AnyRef = null,
			  args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
			  
	def config(message: Any,
			  messageType: String = null,
			  className: String = null,
			  method: String = null,
			  lineNumber: String = null,
			  level: LogLevel = LogLevel.Config,
			  reference: AnyRef = null,
			  args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
	
	val info = Log
	
	def warn(message: Any,
			 messageType: String = null,
			 className: String = null,
			 method: String = null,
			 lineNumber: String = null,
			 level: LogLevel = LogLevel.Warning,
			 reference: AnyRef = null,
			 args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
			 
	def trace(message: Any,
			  throwable: Throwable,
			  messageType: String = "stacktrace",
			  className: String = null,
			  method: String = null,
			  lineNumber: String = null,
			  level: LogLevel = LogLevel.Warning,
			  args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, throwable, args)
	
	def severe(message: Any,
			  messageType: String = null,
			  className: String = null,
			  method: String = null,
			  lineNumber: String = null,
			  level: LogLevel = LogLevel.Severe,
			  reference: AnyRef = null,
			  args: Seq[AnyRef] = null) = Log(message, messageType, className, method, lineNumber, level, reference, args)
			  
	val web = WebLog
}
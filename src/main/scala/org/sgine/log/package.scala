package org.sgine

package object log {
	val log = Log
	def warn(message: String) = Log(message, level = LogLevel.Warning)
}
package org.sgine.log

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class FileLogger(file: File) extends Logger {
	private val out = new BufferedWriter(new FileWriter(file))
	
	def log(log: Log) = {
		out.write(log.toString)
		out.newLine()
	}
}
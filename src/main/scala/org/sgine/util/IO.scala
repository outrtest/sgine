package org.sgine.util

import java.io.InputStream
import java.io.OutputStream

object IO {
	@scala.annotation.tailrec
	def stream(input: InputStream, output: OutputStream, buf: Array[Byte] = new Array[Byte](512)): Unit = {
		val len = input.read(buf)
		if (len != -1) {
			output.write(buf, 0, len)
			stream(input, output, buf)
		}
	}
}
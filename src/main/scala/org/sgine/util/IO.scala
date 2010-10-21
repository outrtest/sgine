package org.sgine.util

import java.io.InputStream
import java.io.OutputStream

import scala.math._

object IO {
	@scala.annotation.tailrec
	def stream(input: InputStream, output: OutputStream, buf: Array[Byte] = new Array[Byte](512), length: Int = -1, written: Int = 0): Unit = {
		val to = if (length == -1) {
			buf.length
		} else {
			min(buf.length, written - length)
		}
		val len = input.read(buf, 0, to)
		if (len != -1) {
			val w = written + len
			output.write(buf, 0, len)
			if ((length == -1) || (w < length)) {
				stream(input, output, buf, length, w)
			}
		}
	}
}
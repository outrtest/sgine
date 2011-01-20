package org.sgine

import java.io._

import scala.math._

package object io {
  @scala.annotation.tailrec
	def stream(input: InputStream, output: OutputStream, buf: Array[Byte] = new Array[Byte](4096), length: Int = -1, written: Int = 0): Unit = {
		val to = if (length == -1) {
			buf.length
		} else {
			min(buf.length, length - written)
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

  def toBytes(value: AnyRef) = {
    val bos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(bos)
    oos.writeObject(value)
    oos.flush()
    bos.close()
    bos.toByteArray()
  }

  def fromBytes(data: Array[Byte], classLoader: ClassLoader) = {
    val bis = new ByteArrayInputStream(data)
    val ois = new ObjectInputStream(bis) {
      override def resolveClass(desc: ObjectStreamClass) = {
        Class.forName(desc.getName, false, classLoader)
      }
    }
    ois.readObject()
  }
}
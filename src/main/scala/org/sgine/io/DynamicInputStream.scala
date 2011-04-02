package org.sgine.io

import java.io.{FileOutputStream, File, DataInputStream, InputStream}

/**
 * 
 * @author mhicks
 * Date: 1/19/11
 * Time: 3:15 PM
 */
class DynamicInputStream(in: InputStream, directory: File, classLoader: ClassLoader = Thread.currentThread.getContextClassLoader) {
   private val input = new DataInputStream(in)

  def read[T]() = {
    // Read the header
    val valueType = input.readInt()
    valueType match {
      case ValueType.Null => null
      case ValueType.File => readFile().asInstanceOf[T]
      case ValueType.Object => readObject().asInstanceOf[T]
    }
  }

  def close() = input.close()

  private def readFile() = {
    // Read the filename
    val name = readObject().asInstanceOf[String]

    // Read the file length
    val length = input.readInt()

    // Save the file locally
    val file = new File(directory, name)
    val output = new FileOutputStream(file)
    try {
      stream(input, output, length = length)
    } finally {
      output.flush()
      output.close()
    }
  }

  private def readObject() = {
    // Read the length
    val length = input.readInt()

    // Restore the object
    val data = new Array[Byte](length)
    input.readFully(data)
    fromBytes(data, classLoader)
  }
}
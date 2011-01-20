package org.sgine.io

import java.io._

/**
 * 
 * @author mhicks
 * Date: 1/19/11
 * Time: 3:06 PM
 */
class DynamicOutputStream(out: OutputStream) {
  private val output = new DataOutputStream(out)

  def write(value: AnyRef) = value match {
    case null => writeNull()
    case file: File => writeFile(file)
    case _ => writeObject(value)
  }

  def flush() = output.flush()

  def close() = output.close()

  private def writeNull() = {
    // Write the header
    output.write(ValueType.Null)
  }

  private def writeFile(file: File) = {
    // Write the header
    output.write(ValueType.File)

    // Write the filename
    directWriteObject(file.getName)

    // Write the file length
    output.writeInt(file.length.toInt)

    // Open input stream for file and transfer
    val input = new FileInputStream(file)
    try {
      stream(input, output, length = file.length.toInt)
    } finally {
      input.close()
    }
  }

  private def writeObject(value: AnyRef) = {
    // Write the header
    output.write(ValueType.Object)

    // Write the object
    directWriteObject(value)
  }

  /**
   * Directly writes an object out without a header
   */
  private def directWriteObject(value: AnyRef) = {
    // Convert the object to a Byte Array
    val data = toBytes(value)

    // Write the length
    output.writeInt(data.length)

    // Write the Byte Array
    output.write(data)
  }
}
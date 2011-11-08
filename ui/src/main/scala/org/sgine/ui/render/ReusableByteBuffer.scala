package org.sgine.ui.render

import java.nio.{ByteOrder, ByteBuffer}
import java.lang.ThreadLocal

/**
 * Provides a thread-local instance of a direct ByteBuffer to be re-used without creating excess garbage or memory
 * consumption.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
private class ReusableByteBuffer {
  private var byteBuffer: ByteBuffer = _

  def allocate(size: Int) = {
    if (byteBuffer == null || byteBuffer.capacity < size) {
      byteBuffer = ReusableByteBuffer.createBuffer(size)
    }
    byteBuffer.clear()

    byteBuffer
  }
}

object ReusableByteBuffer {
  private val instance = new ThreadLocal[ReusableByteBuffer]

  def apply(size: Int)(f: ByteBuffer => Unit) = {
    val rbb = get
    val bb = rbb.allocate(size)
    f(bb)
  }

  def apply(width: Int, height: Int)(f: ByteBuffer => Unit) = {
    val rbb = get
    val bb = rbb.allocate(width * height * 4)
    f(bb)
  }

  private def get = {
    instance.get() match {
      case null => {
        val rbb = new ReusableByteBuffer
        instance.set(rbb)
        rbb
      }
      case rbb => rbb
    }
  }

  private def createBuffer(size: Int) = {
    ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder)
  }
}
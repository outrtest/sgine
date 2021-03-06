package org.sgine.ui.render

import com.badlogic.gdx.Gdx.{gl11 => GL}
import com.badlogic.gdx.graphics._
import java.nio.ByteBuffer

/**
 * ArrayBuffer is used to represent an OpenGL array buffer.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ArrayBuffer(val dynamic: Boolean) {
  private lazy val id = ArrayBuffer.genBuffer()

  private var _data: Seq[Double] = Nil
  private var _size = 0

  def this(_data: Seq[Double]) = {
    this(false)
    data = _data
  }

  def data = _data

  /**
   * Assigns a new set of data to this ArrayBuffer.
   */
  def data_=(_data: Seq[Double]) = {
    GL.glBindBuffer(GL11.GL_ARRAY_BUFFER, id)
    val sizeChanged = _data.length != size
    this._data = _data
    if (sizeChanged) {
      _size = _data.length
      val usage = if (dynamic) {
        GL20.GL_STREAM_DRAW
      }
      else {
        GL11.GL_STATIC_DRAW
      }
      GL.glBufferData(GL11.GL_ARRAY_BUFFER, sizeInBytes, null, usage)
    }
    ReusableByteBuffer(_data.length * 4)(writeData)
  }

  /**
   * The number of entries in this ArrayBuffer.
   */
  def size = _size

  /**
   * The size in bytes of this ArrayBuffer.
   */
  def sizeInBytes = size * 4

  private val writeData = (buffer: ByteBuffer) => {
    data.foreach(d => buffer.putFloat(d.toFloat))
    buffer.flip()
    GL.glBufferSubData(GL11.GL_ARRAY_BUFFER, 0, sizeInBytes, buffer)
  }

  /**
   * Binds the ArrayBuffer to the GL context.
   */
  def bind() = {
    GL.glEnableClientState(GL10.GL_VERTEX_ARRAY)
    GL.glBindBuffer(GL11.GL_ARRAY_BUFFER, id)
  }

  /**
   * Binds the texture coordinates defined given the supplied offset to the GL context.
   */
  def bindTextureCoordinates(offset: Int = 0) = {
    GL.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    GL.glTexCoordPointer(2, GL10.GL_FLOAT, 0, offset * 4)
  }

  /**
   * Draws the vertices specified from the index and vertices arguments.
   */
  def drawVertices(index: Int = 0, vertices: Int = size / 3) = {
    GL.glVertexPointer(3, GL10.GL_FLOAT, 0, 0)
    GL.glDrawArrays(GL10.GL_TRIANGLES, index, vertices)
  }

  /**
   * Deletes the backing buffer.
   *
   * Note: Calling this method destroys the ArrayBuffer and cannot be used again.
   */
  def dispose() = ArrayBuffer.deleteBuffer(id)
}

object ArrayBuffer {
  /**
   * Convenience method to generate a single array buffer.
   */
  def genBuffer() = {
    val array = new Array[Int](1)
    GL.glGenBuffers(1, array, 0)
    array(0)
  }

  /**
   * Convenience method to delete a single array buffer.
   */
  def deleteBuffer(id: Int) = {
    val array = Array(id)
    GL.glDeleteBuffers(1, array, 0)
  }
}
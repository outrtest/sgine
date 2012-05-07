package org.sgine.ui

import uk.co.caprica.vlcj.player.direct.RenderCallback
import com.sun.jna.Memory
import java.io.File
import uk.co.caprica.vlcj.player._
import org.sgine.concurrent.Time

import scala.collection.JavaConversions._
import org.sgine.property.{ObjectPropertyParent, Property}
import com.badlogic.gdx.graphics._
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicReference
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL21._
import java.util.concurrent.ConcurrentLinkedQueue
import annotation.tailrec

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Media extends Image {
  val resource = Property[String]("resource", null)
  val buffers = Property[Int]("buffers", 2)

  object information extends ObjectPropertyParent(this) {
    val length = Property[Long]("length", 0L)
  }

  private val mediaPlayer = Property[MediaPlayer]("mediaPlayer", null)
  private val readBuffer = new AtomicReference[PBO](null)
  private val queue = new ConcurrentLinkedQueue[PBO]()

  private object renderCallback extends MediaPlayerEventAdapter with RenderCallback {
    def display(nativeBuffer: Memory) = {
      queue.poll() match {
        case null => // Nothing to do
        case pbo => {
          val bufferLength = size.width().toInt * size.height().toInt * 4
          val videoBuffer = nativeBuffer.getByteBuffer(0, bufferLength)
          pbo.buffer.put(videoBuffer)
          pbo.buffer.flip()
          readBuffer.set(pbo)
        }
      }
    }

    override def lengthChanged(mediaPlayer: MediaPlayer, newLength: Long) = {
      information.length := newLength
    }
  }

  resource.onChange {
    mediaPlayer() match {
      case null => // Nothing to do
      case mp => {
        mp.stop()
        mp.release()
      }
    }

    // Determine size
    val (width, height) = Media.determineSize(resource())
    size.measured.width := width
    size.measured.height := height

    val w = size.width().toInt
    val h = size.height().toInt
    mediaPlayer := Media.factory.newDirectMediaPlayer("RGBA", w, h, w * 4, renderCallback)
    mediaPlayer().prepareMedia(resource())
  }

  @tailrec
  private def clearQueue(): Unit = {
    val pbo = queue.poll()
    if (pbo != null) {
      pbo.dispose()
      clearQueue()
    }
  }

  override protected def draw() = {
    if (texture() == null) {
      val width = size.width().toInt
      val height = size.height().toInt
      if (width > 0 && height > 0) {
        texture := new Texture(width, height, Pixmap.Format.RGBA8888)

        clearQueue()
        readBuffer.getAndSet(null) match {
          case null => // Ignore
          case pbo => pbo.dispose()
        }
        (0 until buffers()).foreach(i => queue.add(PBO(width, height)))
      }
    }

    // Read the data back to the texture
    readBuffer.getAndSet(null) match {
      case null => // Nothing to read
      case pbo => {
        pbo.updateTexture(texture().getTextureObjectHandle)
        pbo.updateBuffer()
        queue.add(pbo)
      }
    }

    super.draw()
  }

  def snapshot(file: File) = mediaPlayer().saveSnapshot(file)

  def play() = mediaPlayer() match {
    case null => // Ignore
    case mp => mp.play()
  }

  def pause() = mediaPlayer() match {
    case null => // Ignore
    case mp => mp.pause()
  }

  def stop() = mediaPlayer() match {
    case null => // Ignore
    case mp => mp.stop()
  }

  override def destroy() = {
    mediaPlayer().release()

    super.destroy()
  }
}

object Media {
  private val factory = new MediaPlayerFactory("--no-video-title-show")

  def determineSize(resource: String) = {
    var size: (Int, Int) = null
    val mediaPlayer = factory.newDirectMediaPlayer(1, 1, new RenderCallback {
      def display(nativeBuffer: Memory) = {}
    })
    mediaPlayer.mute()
    mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter {
      override def mediaMetaChanged(mediaPlayer: MediaPlayer, metaType: Int) = {
        mediaPlayer.getTrackInfo.find(info => info.isInstanceOf[VideoTrackInfo]) match {
          case None => // Ignore
          case Some(videoTrack: VideoTrackInfo) => {
            val width = videoTrack.width()
            val height = videoTrack.height()
            size = width -> height
          }
        }
      }
    })
    mediaPlayer.prepareMedia(resource)
    mediaPlayer.parseMedia()
    Time.waitFor(10.0) {
      size != null
    }
    mediaPlayer.stop()
    mediaPlayer.release()
    size
  }
}

case class TrackInformation(id: Int,
                            width: Int,
                            height: Int,
                            codec: Int,
                            profile: Int,
                            level: Int)

class PBO private(val id: Int, val width: Int, val height: Int, var buffer: ByteBuffer = null) {
  updateBuffer()

  def updateBuffer() = {
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, id)
    buffer = glMapBuffer(GL_PIXEL_UNPACK_BUFFER, GL_WRITE_ONLY, buffer)
  }

  def updateTexture(textureId: Int) = {
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, id)
    glUnmapBuffer(GL_PIXEL_UNPACK_BUFFER)
    glBindTexture(GL_TEXTURE_2D, textureId)
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, 0)
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
  }

  def dispose() = {
    glDeleteBuffers(id)
  }
}

object PBO {
  def apply(width: Int, height: Int) = {
    val id = glGenBuffers()
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, id)
    glBufferData(GL_PIXEL_UNPACK_BUFFER, width * height * 4, GL_STREAM_DRAW)
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0)
    new PBO(id, width, height)
  }
}
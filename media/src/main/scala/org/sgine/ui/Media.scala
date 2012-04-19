package org.sgine.ui

import uk.co.caprica.vlcj.player.direct.RenderCallback
import com.sun.jna.Memory
import java.io.File
import uk.co.caprica.vlcj.player._
import org.sgine.concurrent.Time
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.ConcurrentLinkedQueue
import com.badlogic.gdx.graphics.{Texture, Pixmap}

import scala.collection.JavaConversions._
import org.sgine.property.{ObjectPropertyParent, Property}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Media extends Image {
  val resource = Property[String]("resource")
  val buffers = Property[Int]("buffers", 2)

  object information extends ObjectPropertyParent(this) {
    val length = Property[Long]("length")
  }

  private val mediaPlayer = Property[MediaPlayer]("mediaPlayer")
  private val currentBuffer = new AtomicReference[Pixmap](null)
  private val availableBuffers = new ConcurrentLinkedQueue[Pixmap]()
  @volatile private var usedBuffers = 0

  private object renderCallback extends MediaPlayerEventAdapter with RenderCallback {
    def display(nativeBuffer: Memory) = availableBuffers.poll() match {
      case null => // No buffer to render to
      case pixmap: Pixmap => {
        val pixels = pixmap.getPixels
        val bufferLength = (size.width().toInt * size.height().toInt * 4) + 32
        val buffer = nativeBuffer.getByteBuffer(0, bufferLength)
        pixels.clear()
        buffer.position(buffer.limit() - 32)
        buffer.flip()
        pixels.put(buffer)
        pixels.flip()
        currentBuffer.getAndSet(pixmap) match {
          case null => // No previous set - nothing to do
          case previous => availableBuffers.add(previous) // Unused update, recycle
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
    measured.width := width
    measured.height := height

    val w = size.width().toInt
    val h = size.height().toInt
    mediaPlayer := Media.factory.newDirectMediaPlayer("RGBA", w, h, w * 4, renderCallback)
    val pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888)
    texture := null
    availableBuffers.add(pixmap)
    mediaPlayer().prepareMedia(resource())
  }

  override protected def draw() = {
    currentBuffer.getAndSet(null) match {
      case null => // Nothing to update
      case p: Pixmap => {
        val width = size.width().toInt
        val height = size.height().toInt
        val pixmap = if (p.getWidth != width || p.getHeight != height) {
          new Pixmap(width, height, Pixmap.Format.RGBA8888)
        } else {
          p
        }
        pixmap.getWidth

        if (texture() == null) {
          texture := new Texture(pixmap)
        }

        texture().draw(pixmap, 0, 0)

        if (usedBuffers > buffers()) {
          // Buffers reduced, don't recycle
          usedBuffers -= 1
          pixmap.dispose()
        } else {
          availableBuffers.add(pixmap)
        }
      }
    }

    if (usedBuffers < buffers()) {
      availableBuffers.add(new Pixmap(size.width().toInt, size.height().toInt, Pixmap.Format.RGBA8888))
      usedBuffers += 1
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

  def main(args: Array[String]): Unit = {
    //    val file = new File("test.avi")
    //    val t1 = Time.elapsed {
    //      println(determineSize(file.getAbsolutePath))
    //    }
    //    val t2 = Time.elapsed {
    //      println(determineSize("test.mp4"))
    //    }
    //    println("Took: %s, Took: %s".format(t1, t2))
    //    determineInformation("test.avi")
  }
}

case class TrackInformation(id: Int,
                            width: Int,
                            height: Int,
                            codec: Int,
                            profile: Int,
                            level: Int)
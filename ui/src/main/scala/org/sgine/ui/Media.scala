package org.sgine.ui

import com.badlogic.gdx.graphics.{Pixmap, Texture}
import uk.co.caprica.vlcj.player.direct.RenderCallback
import com.sun.jna.Memory
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener
import uk.co.caprica.vlcj.player.{VideoTrackInfo, MediaPlayer, MediaPlayerFactory}
import org.sgine.property.{MutableProperty, Property}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Media extends RenderableComponent {
  val resource = Property[String]()
  val loaded = Property[Boolean]()

  resource.onChange {
    load()
  }

  @volatile private var texture: Texture = _
  @volatile private var pixmap: Pixmap = _
  @volatile private var updates = false

  def load() = Media.load(this)

  def play() = {
    stop()
    load()

    createMediaPlayer()
    pixmap = new Pixmap(information.width(), information.height(), Pixmap.Format.RGBA8888)
    texture = new Texture(pixmap)
    mediaPlayer.playMedia(resource())
  }

  def pause() = if (mediaPlayer != null) {
    mediaPlayer.pause()
  }

  def stop() = {
    if (mediaPlayer != null) {
      mediaPlayer.stop()
    }

    if (texture != null) {
      texture.dispose()
      texture = null
    }
    if (pixmap != null) {
      pixmap.dispose()
      pixmap = null
    }
  }

  object information {
    val width: Property[Int] = Property[Int]()
    val height: Property[Int] = Property[Int]()
    val length: Property[Long] = Property[Long]()
  }

  private var mediaPlayer: MediaPlayer = _

  private def createMediaPlayer() = {
    if (mediaPlayer != null) {
      mediaPlayer.release()
    }
    mediaPlayer = Media.factory.newDirectMediaPlayer("RGBA", information.width(), information.height(), information.width() * 4, new RenderCallback {
      def display(nativeBuffer: Memory) = {
        if (pixmap != null) {
          try {
            val videoBuffer = (information.width() * information.height() * 4) + 32
            val buffer = pixmap.getPixels
            val b = nativeBuffer.getByteBuffer(0, videoBuffer)
            buffer.clear()
            b.position(b.limit() - 32) // Remove last 32 bytes
            b.flip()
            buffer.put(b)
            buffer.flip()
            updates = true
          } catch {
            case exc => {
              exc.printStackTrace()
              System.exit(1)
            }
          }
        }
      }
    })
  }

  resource.onChange {
    pixmap = null
  }

  def render() = {
    if (updates) {
      updates = false
      texture.draw(pixmap, 0, 0)
    }
    if (texture != null) {
      Component.batch.get().draw(texture, location.x().toFloat, location.y().toFloat)
    }
  }

  override def destroy() = {
    mediaPlayer.release()

    super.destroy()
  }
}

object Media {
  private var media: Media = _

  private val factory = new MediaPlayerFactory()
  private val mediaPlayer = Media.factory.newDirectMediaPlayer(1, 1, new RenderCallback {
    def display(nativeBuffer: Memory) = {
    }
  })
  mediaPlayer.mute()
  mediaPlayer.addVideoOutputEventListener(new VideoOutputEventListener {
    def videoOutputAvailable(mediaPlayer: MediaPlayer, videoOutput: Boolean) = {
      val trackInfo = mediaPlayer.getTrackInfo.get(0).asInstanceOf[VideoTrackInfo]
      media.information.width.asInstanceOf[MutableProperty[Int]] := trackInfo.width()
      media.information.height.asInstanceOf[MutableProperty[Int]] := trackInfo.height()
      media.information.length.asInstanceOf[MutableProperty[Long]] := mediaPlayer.getLength
      media.loaded := true
    }
  })

  private def load(media: Media) = synchronized {
    this.media = media

    media.loaded := false
    mediaPlayer.prepareMedia(media.resource())
    mediaPlayer.parseMedia()
    mediaPlayer.start()
    // TODO: replace below with asynchronous listener
    while (!media.loaded()) {
      Thread.sleep(1)
    }
    mediaPlayer.stop()

    this.media = null
  }
}
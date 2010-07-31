package org.sgine.ui

import com.sun.media.jmc.Media
import com.sun.media.jmc.MediaProvider
import com.sun.media.jmc.control.AudioControl
import com.sun.media.jmc.control.VideoDataBuffer
import com.sun.media.jmc.control.VideoRenderControl
import com.sun.media.jmc.event.VideoRendererEvent
import com.sun.media.jmc.event.VideoRendererListener

import java.nio.ByteBuffer
import java.nio.IntBuffer

import org.lwjgl.opengl.GL12._

import org.sgine.core.Resource

import org.sgine.event.EventHandler
import org.sgine.event.ProcessingMode

import org.sgine.property.AdvancedProperty
import org.sgine.property.event.PropertyChangeEvent

import org.sgine.render.RenderImage

import org.sgine.ui.ext.AdvancedComponent

import scala.math._

class MediaPlayer extends CachedComponent with AdvancedComponent {
	val source = new AdvancedProperty[Resource](null, this)
	val fader = new AdvancedProperty[Double](0.0, this)
	val volume = new AdvancedProperty[Double](1.0, this)
	val balance = new AdvancedProperty[Double](0.0, this)
	val mute = new AdvancedProperty[Boolean](false, this)
	val repeat = new AdvancedProperty[Boolean](false, this)
	
	private val provider = new MediaProvider()
	private val dataBuffer = new VideoDataBuffer(null, 0, 0, 0, VideoDataBuffer.Format.BGR)
	private var media: Media = _
	private var videoControl: VideoRenderControl = _
	private var audioControl: AudioControl = _
	
	initialize()
	
	private def initialize() = {
		pixelFormat := GL_BGRA
		
		source.listeners += EventHandler(sourceChanged, processingMode = ProcessingMode.Blocking)
		repeat.listeners += EventHandler(repeatChanged, processingMode = ProcessingMode.Blocking)
	}
	
	def play() = {
		provider.play()
	}
	
	protected def draw(buffer: ByteBuffer): Unit = {
		// TODO: optimize
		videoControl.getData(dataBuffer)
		val b = dataBuffer.getBuffer.asInstanceOf[IntBuffer]
		while (b.hasRemaining) {
			buffer.putInt(b.get)
		}
		videoControl.releaseData(dataBuffer)
	}
	
	override protected def updateLocalMatrix() = {
		super.updateLocalMatrix()
		
		// Correction for video display
		localMatrix := localMatrix().rotateY(Pi)
		localMatrix := localMatrix().rotateZ(Pi)
	}
	
	private def sourceChanged(evt: PropertyChangeEvent[Resource]) = {
		media = new Media(source().url.toURI)
		provider.setPlayerPeerFromMedia(media)
		videoControl = provider.getControl(classOf[VideoRenderControl])
		audioControl = provider.getControl(classOf[AudioControl])
		
		videoControl.addVideoRendererListener(new VideoRendererListener() {
			def videoFrameUpdated(evt: VideoRendererEvent) = {
				invalidateCache()
			}
		})
		
		audioControl.setFader(fader().toFloat)
		audioControl.setBalance(balance().toFloat)
		audioControl.setVolume(volume().toFloat)
		audioControl.setMute(mute())
		
		width := videoControl.getFrameWidth
		height := videoControl.getFrameHeight
	}
	
	private def repeatChanged(evt: PropertyChangeEvent[Boolean]) = {
		provider.setRepeating(repeat())
	}
}
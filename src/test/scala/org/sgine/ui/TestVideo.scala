package org.sgine.ui

import java.nio._

import com.sun.media.jmc._
import com.sun.media.jmc.control._
import com.sun.media.jmc.event._

import org.sgine.core.Color
import org.sgine.core.Resource

import org.sgine.render.Renderer
import org.sgine.render.scene.RenderableScene

import org.sgine.scene.GeneralNodeContainer
import org.sgine.scene.ext.ResolutionNode

import scala.collection.JavaConversions._
import scala.math._

object TestVideo {
	val component = new CachedComponent() {
		protected def draw(buffer: ByteBuffer): Unit = {
			updateVideo(buffer)
		}
	}
	val listener = new MediaListener()
	val provider = new MediaProvider()
	provider.addMediaTimerTaskCallback(listener)
	var vc: VideoRenderControl = _
	var ac: AudioControl = _
	var r: java.awt.Rectangle = _
//	var frameSize: java.awt.Dimension = _
	var playing = false
	
	private var vdb: VideoDataBuffer = _
	
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Video")
		r.verticalSync := false
		r.background := Color.Green
		
		val scene = new GeneralNodeContainer() with ResolutionNode
		scene.setResolution(1024, 768)
		
		val uri = new java.net.URL("http://sun.edgeboss.net/download/sun/media/1460825906/1460825906_2956241001_big-buck-bunny-640x360.flv").toURI
		val media = new Media(uri)
		provider.setPlayerPeerFromMedia(media)
		vc = provider.getControl(classOf[VideoRenderControl])
		vc.addVideoRendererListener(listener)
		ac = provider.getControl(classOf[AudioControl])
		ac.setBalance(0.0f)
		ac.setFader(0.0f)
		ac.setMute(false)
		ac.setVolume(1.0f)
		
//		frameSize = vc.getFrameSize
//		println("SIZE: " + frameSize.getWidth + "x" + frameSize.getHeight)
		
//		component.width := frameSize.getWidth.toInt
//		component.height := frameSize.getHeight.toInt
		component.width := vc.getFrameWidth
		component.height := vc.getFrameHeight
		vdb = new VideoDataBuffer(null, component.width(), component.height(), 0, VideoDataBuffer.Format.BGR)
//		component.painter := testPaint
		scene += component
		
		provider.play()
		
		r.renderable := RenderableScene(scene)
	}
	
	def updateVideo(buffer: ByteBuffer): Unit = {
		vc.releaseData(vdb)
		vdb.setBuffer(buffer)
		vc.getData(vdb)
		buffer.rewind()
		buffer.flip()
		
//		buffer.flip()
//		vc.releaseData(vdb)
	}
	
	def testPaint(g: java.awt.Graphics2D) = {
//		val r = new java.awt.Rectangle(0, 0, frameSize.getWidth.toInt, frameSize.getHeight.toInt)
//		g.translate(0, r.height)
//		vc.paintVideoFrame(g, r)
	}
	
	class MediaListener extends MediaProvider.MediaTimerTaskCallback with VideoRendererListener {
		def videoFrameUpdated(evt: VideoRendererEvent) = {
			playing = true
			component.invalidateDrawing()
		}
		
		def invoke() = {
//			println("INVOKE!")
			val current = provider.getMediaTime
		}
	}
}
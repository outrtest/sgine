package org.sgine.opengl

import java.util.concurrent._

import scala.collection.JavaConversions._

import org.sgine.util._
import org.sgine.work._

import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu.GLU._

import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Container
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

class GLWindow private (val frame: Frame) extends GLContainer {
	val awtContainer = frame
	
	override protected def destroyAWT() = {
		frame.dispose()
	}
}

object GLWindow {
	def apply(title: String, width: Int, height: Int, workManager: WorkManager = DefaultWorkManager): GLWindow = {
		// Create the frame
		val frame = new Frame()
		frame.setSize(width, height)
		frame.setTitle(title)
		frame.setResizable(false)		// TODO: support resizing at some point
		
		val w = new GLWindow(frame)
		w.containerWidth := width
		w.containerHeight := height
		w.workManager := workManager
		
		frame.addWindowListener(new WindowAdapter() {
			override def windowClosing(e:WindowEvent):Unit = {
				w.keepAlive = false
			}
		})
		
		w
	}
}
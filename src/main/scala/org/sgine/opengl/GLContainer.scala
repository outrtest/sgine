package org.sgine.opengl

import org.sgine.property._

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

trait GLContainer {
	val awtContainer: Container
	
	val containerWidth = new AdvancedProperty[Int](0)
	val containerHeight = new AdvancedProperty[Int](0)
	val fullscreen = new AdvancedProperty[Boolean](false)
	val verticalSync = new AdvancedProperty[Boolean](true)
	val workManager = new MutableProperty[WorkManager](DefaultWorkManager)
	
	protected var keepAlive = true
	private var renders:Long = 0
	private var lastRender:Long = System.nanoTime()
	private val canvas = new Canvas()
	private val thread = new Thread(FunctionRunnable(run))
	
	val displayables = new ConcurrentLinkedQueue[(Double) => Unit]()
	
	def begin() = {
		thread.start()
		
		while(renders < 2) {
			Thread.sleep(10)
		}
	}
	
	private def run() = {
		awtContainer.setLayout(new BorderLayout())
		awtContainer.add(BorderLayout.CENTER, canvas)
		
		awtContainer.setVisible(true)
		
		// Configure display
		Display.setFullscreen(fullscreen())		// TODO: revisit
		Display.setVSyncEnabled(verticalSync())		// TODO: revisit
		Display.setDisplayMode(determineDisplayMode())
		Display.setParent(canvas)
		Display.create()		// TODO: incorporate PixelFormat
		input.Keyboard.validate()
		
		// Initialize the GL context
		initGL()
		
		while(keepAlive) {
			Display.update()
			
			if (Display.isCloseRequested()) {		// Window close
				keepAlive = false
			}
			render()
		}
		Display.destroy()
		destroyAWT()
		System.exit(0)			// TODO: remove
	}
	
	protected def destroyAWT() = {
	}
	
	private def determineDisplayMode():DisplayMode = {		// TODO: make better
		var fallbackMode: DisplayMode = Display.getDisplayMode()
		println("Desktop Display: " + Display.getDesktopDisplayMode())
		println("Display Mode: " + Display.getDisplayMode())
		for (mode <- Display.getAvailableDisplayModes()) {
			fallbackMode = mode
			if (mode.getWidth() == containerWidth()) {
				if (mode.getHeight() == containerHeight()) {
					println("Changing to: " + mode)
					return mode
				}
			}
		}
		return fallbackMode
	}
	
	def initGL() = {
		glClearDepth(1.0)
		glEnable(GL_BLEND)
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)
		glEnable(GL_TEXTURE_2D)
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
		reshapeGL(0, 0, containerWidth(), containerHeight())
	}
 
	def reshapeGL(x:Int, y:Int, width:Int, height:Int) = {
	    val h = width.toFloat / height.toFloat
	    glViewport(0, 0, width, height)
	    glMatrixMode(GL_PROJECTION)
	    glLoadIdentity()
	    gluPerspective(45.0f, h, 1.0f, 20000.0f)
	    glMatrixMode(GL_MODELVIEW)
	    glLoadIdentity()
	}
 
	def render() = {
		val currentRender = System.nanoTime()
		val time = (currentRender - lastRender) / 1000000000.0
		
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
	
	    glLoadIdentity()
	    glColor3f(1.0f, 1.0f, 1.0f)
	    
	    displayables.foreach(_(time))
	    
	    renders += 1
	    if (renders == Long.MaxValue) {
	    	renders = 0
	    }
	    
	    lastRender = currentRender
	}
}
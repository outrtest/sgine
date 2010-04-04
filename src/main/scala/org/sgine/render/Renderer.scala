package org.sgine.render

import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11._

import org.lwjgl.util.glu.GLU._

import org.sgine.input.Keyboard
import org.sgine.input.Mouse

import org.sgine.property.AdvancedProperty
import org.sgine.property.TransactionalProperty

import org.sgine.property.container.PropertyContainer

import org.sgine.util.FunctionRunnable

class Renderer extends PropertyContainer {
	private var rendered = false
	private var keepAlive = true
	private var lastRender = -1L
	
	val canvas = new java.awt.Canvas()
	lazy val thread = new Thread(FunctionRunnable(run))
	
	val fullscreen = new AdvancedProperty[Boolean](false, this) with TransactionalProperty[Boolean]
	val verticalSync = new AdvancedProperty[Boolean](true, this) with TransactionalProperty[Boolean]
	val renderable = new AdvancedProperty[Renderable](null, this)
	
	def start() = {
		thread.start()
		
		waitForRender()
	}
	
	def isAlive = keepAlive
	
	def shutdown() = keepAlive = false
	
	private def run(): Unit = {
		initGL()

		while ((keepAlive) && (!Display.isCloseRequested)) {
			Display.update()
			
			render()
		}
		
		keepAlive = false
		
		destroy()
	}
	
	
	private def initGL() = {
		Display.setFullscreen(fullscreen())
		Display.setVSyncEnabled(verticalSync())
		Display.setParent(canvas)
		Display.create()
		
		glClearDepth(1.0)
		glEnable(GL_BLEND)
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)
		glEnable(GL_TEXTURE_2D)
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
		
		Keyboard.validate()
		Mouse.validate()
		
		reshapeGL()
	}
	
	private def reshapeGL() = {
		val width = canvas.getWidth
		val height = canvas.getHeight
		val h = width.toFloat / height.toFloat
		glViewport(0, 0, width, height)
		glMatrixMode(GL_PROJECTION)
		glLoadIdentity()
		gluPerspective(45.0f, h, 1.0f, 20000.0f)
		glMatrixMode(GL_MODELVIEW)
		glLoadIdentity()
	}
	
	private def render() = {
		if (fullscreen.uncommitted) {
			fullscreen.commit()
			Display.setFullscreen(fullscreen())
		}
		if (verticalSync.uncommitted) {
			verticalSync.commit()
			Display.setVSyncEnabled(verticalSync())
		}
		
		val currentRender = System.nanoTime
		if (lastRender != -1) {
			val time = (currentRender - lastRender) / 1000000000.0
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			
			glLoadIdentity()
			glColor3f(1.0f, 1.0f, 1.0f)
			
			Renderer.time.set(time)
			Renderer.fps.set((1.0 / time).round.toInt)
			
			val r = renderable()
			if (r != null) r.render()
		}
		lastRender = currentRender
		
		rendered = true
		thread.synchronized {
			thread.notifyAll()
		}
	}
	
	def waitForRender() = {
		thread.synchronized {
			while (!rendered) thread.wait()
		}
	}
	
	private def destroy() = {
		Display.destroy()
	}
}

object Renderer {
	val time = new ThreadLocal[Double]
	val fps = new ThreadLocal[Int]
	
	def createFrame(width: Int, height: Int, title: String) = {
		val r = new Renderer()
		
		val f = new java.awt.Frame
		f.setSize(width, height)
		f.setTitle(title)
		f.setResizable(false)
		f.setLayout(new java.awt.BorderLayout())
		f.addFocusListener(new java.awt.event.FocusAdapter() {
			override def focusGained(e: java.awt.event.FocusEvent) = {
				r.canvas.requestFocus()
			}
		});
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			override def windowClosing(e: java.awt.event.WindowEvent) = {
				r.shutdown()
				f.dispose()
			}
		})
		
		r.canvas.setSize(width, height)		// TODO: fix
		f.add(java.awt.BorderLayout.CENTER, r.canvas)
		
		f.setVisible(true)
		
		r.start()
		
		r
	}
}
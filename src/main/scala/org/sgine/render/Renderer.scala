package org.sgine.render

import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.PixelFormat

import org.lwjgl.util.glu.GLU._

import org.sgine.core.Color

import org.sgine.input.Keyboard
import org.sgine.input.Mouse

import org.sgine.math.Matrix4
import org.sgine.math.mutable.{Matrix4 => MutableMatrix4}
import org.sgine.math.mutable.{Ray => MutableRay}
import org.sgine.math.mutable.{Vector3 => MutableVector3}

import org.sgine.property.AdvancedProperty
import org.sgine.property.TransactionalProperty

import org.sgine.property.container.PropertyContainer

import org.sgine.work.Updatable

import org.sgine.util.FunctionRunnable

class Renderer(alpha: Int = 0, depth: Int = 8, stencil: Int = 0, samples: Int = 0, bpp: Int = 0, auxBuffers: Int = 0, accumBPP: Int = 0, accumAlpha: Int = 0, stereo: Boolean = false, floatingPoint: Boolean = false) extends PropertyContainer {
	private var rendered = false
	private var keepAlive = true
	private var lastRender = -1L

	val nearDistance = 1.0
	val farDistance = 20000.0
	
	private val storeRay = MutableRay()
	private val storeMatrix = MutableMatrix4()
	
	val canvas = new java.awt.Canvas()
	lazy val thread = new Thread(FunctionRunnable(run))
	
	val fullscreen = new AdvancedProperty[Boolean](false, this) with TransactionalProperty[Boolean]
	val verticalSync = new AdvancedProperty[Boolean](true, this) with TransactionalProperty[Boolean]
	val renderable = new AdvancedProperty[Renderable](null, this)
	val background = new AdvancedProperty[Color](Color.Black, this) with TransactionalProperty[Color]
	
	def start() = {
		thread.start()
		
		waitForRender()
	}
	
	def isAlive = keepAlive
	
	def shutdown() = keepAlive = false
	
	private def run(): Unit = {
		try {
			initGL()
	
			while ((keepAlive) && (!Display.isCloseRequested)) {
				Display.update()
				
				if (!Updatable.useWorkManager) {
					Updatable.update()
				}
				render()
			}
		} catch {
			case t: Throwable => t.printStackTrace()
		} finally {
			keepAlive = false
			
			destroy()
			System.exit(0)
		}
	}
	
	
	private def initGL() = {
		Display.setFullscreen(fullscreen())
		Display.setVSyncEnabled(verticalSync())
		Display.setParent(canvas)
		
		val format = new PixelFormat(bpp, alpha, depth, stencil, samples, auxBuffers, accumBPP, accumAlpha, stereo, floatingPoint)
		Display.create(format)
		
		glClearDepth(1.0)
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
		glEnable(GL_BLEND)
		glEnable(GL_DEPTH_TEST)
		glDepthFunc(GL_LEQUAL)
		glEnable(GL_POLYGON_OFFSET_FILL)
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
		glFrustum(-1.0f * h, 1.0f * h, -1.0f, 1.0f, nearDistance, farDistance)
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
		if (background.uncommitted) {
			background.commit()
			val c = background()
			glClearColor(c.red.toFloat, c.green.toFloat, c.blue.toFloat, c.alpha.toFloat)
		}
		
		val currentRender = System.nanoTime
		if (lastRender != -1) {
			val time = (currentRender - lastRender) / 1000000000.0
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			
			glLoadIdentity()
			glColor3f(1.0f, 1.0f, 1.0f)
			
			Renderer.time.set(time)
			Renderer.fps.set((1.0 / time).round.toInt)
			
			Mouse.update(this)
			
			val r = renderable()
			if (r != null) r.render(this)
		}
		lastRender = currentRender
		
		rendered = true
		thread.synchronized {
			thread.notifyAll()
		}
	}
	
	def translateLocal(x: Double, y: Double, m: Matrix4, store: MutableVector3) = {
		synchronized {
			storeRay.origin.set(0.0, 0.0, 0.0)
			storeRay.direction.set(x, y, -nearDistance)
			
			storeMatrix.set(m)
			storeMatrix.invert()
			storeRay.transform(storeMatrix)
			storeRay.translateLocal(store)
			
			store
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
	
	def createFrame(width: Int, height: Int, title: String, alpha: Int = 0, depth: Int = 8, stencil: Int = 0, samples: Int = 0, bpp: Int = 0, auxBuffers: Int = 0, accumBPP: Int = 0, accumAlpha: Int = 0, stereo: Boolean = false, floatingPoint: Boolean = false) = {
		val r = new Renderer(alpha, depth, stencil, samples, bpp, auxBuffers, accumBPP, accumAlpha, stereo, floatingPoint)
		
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
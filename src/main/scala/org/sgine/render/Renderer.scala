package org.sgine.render

import java.awt.event.HierarchyEvent
import java.awt.event.HierarchyListener

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.DoubleBuffer

import org.lwjgl.opengl.{Display => GLDisplay}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.PixelFormat

import org.lwjgl.util.glu.GLU._

import org.sgine.core.Color

import org.sgine.input.Keyboard
import org.sgine.input.Mouse

import org.sgine.math.mutable._

import org.sgine.property.AdvancedProperty
import org.sgine.property.TransactionalProperty

import org.sgine.property.container.PropertyContainer

import org.sgine.work.Updatable

import org.sgine.util.FunctionRunnable

import simplex3d.math._
import simplex3d.math.doublem._
import simplex3d.math.doublem.DoubleMath._

class Renderer(alpha: Int = 0, depth: Int = 8, stencil: Int = 0, samples: Int = 0, bpp: Int = 0, auxBuffers: Int = 0, accumBPP: Int = 0, accumAlpha: Int = 0, stereo: Boolean = false, floatingPoint: Boolean = false) extends PropertyContainer {
	private var rendered = false
	private var keepAlive = true
	private var lastRender = -1L

	val nearDistance = 100.0
	val farDistance = 2000.0
	
	val canvas = new java.awt.Canvas()
	canvas.addComponentListener(new java.awt.event.ComponentAdapter() {
		override def componentResized(e: java.awt.event.ComponentEvent) = {
			resized.set(true)
		}
	})
	lazy val thread = new Thread(FunctionRunnable(run))
	
	val fullscreen = new AdvancedProperty[Boolean](false, this) with TransactionalProperty[Boolean]
	val verticalSync = new AdvancedProperty[Boolean](true, this) with TransactionalProperty[Boolean]
	val renderable = new AdvancedProperty[Renderable](null, this)
	val background = new AdvancedProperty[Color](Color.Black, this) with TransactionalProperty[Color]
	
	private val resized = new java.util.concurrent.atomic.AtomicBoolean(false)
	
	def start() = {
		thread.start()
		
		waitForRender()
	}
	
	def isAlive = keepAlive
	
	def shutdown() = keepAlive = false
	
	private def run(): Unit = {
		try {
			initGL()
			while ((keepAlive) && (!GLDisplay.isCloseRequested)) {
				GLDisplay.update()
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
		GLDisplay.setFullscreen(fullscreen())
		GLDisplay.setVSyncEnabled(verticalSync())
		GLDisplay.setParent(canvas)
		
		val format = new PixelFormat(bpp, alpha, depth, stencil, samples, auxBuffers, accumBPP, accumAlpha, stereo, floatingPoint)
		GLDisplay.create(format)
		
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
		if (resized.compareAndSet(true, false)) {
			reshapeGL()
		}
		if (fullscreen.uncommitted) {
			fullscreen.commit()
			GLDisplay.setFullscreen(fullscreen())
		}
		if (verticalSync.uncommitted) {
			verticalSync.commit()
			GLDisplay.setVSyncEnabled(verticalSync())
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
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
			
			Renderer.time.set(time)
			Renderer.maxTime.set(max(time, Renderer.maxTime.get))
			Renderer.fps.set((1.0 / time).round.toInt)
			
			Mouse.update(this)
			
			val r = renderable()
			if (r != null) Renderable.render(this, r)
		}
		lastRender = currentRender
		
		rendered = true
		thread.synchronized {
			thread.notifyAll()
		}
	}

	// TODO: add Camera, port to use Camera
	def screenToWorldCoords(x: Double, y: Double, m: Mat3x4d) = {
		val ray = Ray(Vec3d(0), Vec3d(x, y, -nearDistance))
		ray.transform(inverse(m))
		ray.translate()
	}
	
	def waitForRender() = {
		thread.synchronized {
			while (!rendered) thread.wait()
		}
	}
	
	private def destroy() = {
		GLDisplay.destroy()
	}
}

object Renderer {
	val time = new ThreadLocal[Double]
	val maxTime = new ThreadLocal[Double]
	val fps = new ThreadLocal[Int]
	val matrixStore = new ThreadLocal[DoubleBuffer]
	
	def createFrame(width: Int, height: Int, title: String, alpha: Int = 0, depth: Int = 8, stencil: Int = 0, samples: Int = 0, bpp: Int = 0, auxBuffers: Int = 0, accumBPP: Int = 0, accumAlpha: Int = 0, stereo: Boolean = false, floatingPoint: Boolean = false) = {
		val r = new Renderer(alpha, depth, stencil, samples, bpp, auxBuffers, accumBPP, accumAlpha, stereo, floatingPoint)
		
		val f = new java.awt.Frame
		f.setSize(width, height)
		f.setTitle(title)
		f.setResizable(true)
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
		f.addComponentListener(new java.awt.event.ComponentAdapter() {
			override def componentResized(e: java.awt.event.ComponentEvent) = {
				r.canvas.setSize(f.getSize)
			}
		})
		
		r.canvas.setSize(width, height)		// TODO: fix
		f.add(java.awt.BorderLayout.CENTER, r.canvas)
		
		f.setVisible(true)
		
		r.start()
		
		r
	}
	
	def createCanvas(width: Int, height: Int, title: String, alpha: Int = 0, depth: Int = 8, stencil: Int = 0, samples: Int = 0, bpp: Int = 0, auxBuffers: Int = 0, accumBPP: Int = 0, accumAlpha: Int = 0, stereo: Boolean = false, floatingPoint: Boolean = false) = {
		val r = new Renderer(alpha, depth, stencil, samples, bpp, auxBuffers, accumBPP, accumAlpha, stereo, floatingPoint)
		
		r.canvas.addHierarchyListener(new HierarchyListener() {
			private var changed = false
			
			def hierarchyChanged(evt: HierarchyEvent) = {
				if (!changed) {
					r.start()
					changed = true
				}
			}
		})
		
		r
	}
	
	def loadMatrix(m: Mat3x4d) = {
		val store = matrixStore.get match {
			case null => {
				val b = ByteBuffer.allocateDirect(128).order(ByteOrder.nativeOrder).asDoubleBuffer
				matrixStore.set(b)
				b
			}
			case s: DoubleBuffer => s
		}
		matrixToBuffer(m, store)
		store.flip()
		glLoadMatrix(store)
	}
}
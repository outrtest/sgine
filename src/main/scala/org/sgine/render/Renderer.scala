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

class Renderer extends PropertyContainer {
	private var rendered = false
	private var keepAlive = true
	private var lastRender = -1L

	val nearDistance = 100.0
	val farDistance = 2000.0
	val fieldOfView = radians(45.0)
	
	var time = 0.0
	var fps = 0
	
	val camera = new Camera()
	
	val matrixStore = ByteBuffer.allocateDirect(128).order(ByteOrder.nativeOrder).asDoubleBuffer
	
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
	val settings = new AdvancedProperty[RenderSettings](RenderSettings.Default, this) with TransactionalProperty[RenderSettings]
	
	private var _lights = false
	def lights = _lights
	val light0 = new Light(0, this)
	val light1 = new Light(1, this)
	val light2 = new Light(2, this)
	val light3 = new Light(3, this)
	val light4 = new Light(4, this)
	val light5 = new Light(5, this)
	val light6 = new Light(6, this)
	val light7 = new Light(7, this)
	
	val fog = new Fog(this)
	
	private val resized = new java.util.concurrent.atomic.AtomicBoolean(false)
	
	private val invokeQueue = new java.util.concurrent.ConcurrentLinkedQueue[Function0[Unit]]
	
	def start() = {
		thread.start()
		
		waitForRender()
	}
	
	def isAlive = keepAlive
	
	def shutdown() = keepAlive = false
	
	private def run(): Unit = {
		try {
			thread.setName("renderer")
			initGL()
			Renderer.instance.set(this)
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
	
	private[render] def updateLighting() = {
		if ((light0.enabled()) || (light1.enabled()) || (light2.enabled()) || (light3.enabled()) || (light4.enabled()) || (light5.enabled()) || (light6.enabled()) || (light7.enabled())) {
			glEnable(GL_LIGHTING)
			_lights = true
		} else {
			glDisable(GL_LIGHTING)
			_lights = false
		}
	}
	
	private def initGL() = {
		GLDisplay.setFullscreen(fullscreen())
		GLDisplay.setVSyncEnabled(verticalSync())
		GLDisplay.setParent(canvas)
		
		settings.commit()
		val format = new PixelFormat(
										settings().bpp,
										settings().alpha,
										settings().depth,
										settings().stencil,
										settings().samples,
										settings().auxBuffers,
										settings().accumBPP,
										settings().accumAlpha,
										settings().stereo,
										settings().floatingPoint
									)
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
		
		camera.projection := perspectiveProj(fieldOfView, h, nearDistance, farDistance)
		
		glMatrixMode(GL_PROJECTION)
		matrixToBuffer(camera.projection, matrixStore)
		matrixStore.flip()
		glLoadMatrix(matrixStore)
		
		glMatrixMode(GL_MODELVIEW)
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
		if (settings.uncommitted) {
			throw new RuntimeException("Changing of settings after renderer has started is not currently supported.")
		}
		
		val currentRender = System.nanoTime
		if (lastRender != -1) {
			val time = (currentRender - lastRender) / 1000000000.0
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			
			glLoadIdentity()
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
			
			this.time = time
			//Renderer.maxTime.set(max(time, Renderer.maxTime.get))
			fps = (1.0 / time).round.toInt
			
			Mouse.update(this)
			camera.update()
			
			light0.update()
			light1.update()
			light2.update()
			light3.update()
			light4.update()
			light5.update()
			light6.update()
			light7.update()
			
			fog.update()
			
			invokeQueue.poll() match {
				case null =>
				case f => f()
			}
			
			val r = renderable()
			if (r != null) Renderable.render(this, r)
		}
		lastRender = currentRender
		
		rendered = true
		thread.synchronized {
			thread.notifyAll()
		}
	}

	def screenToWorldCoords(screenCoords: inVec3d) = {
		// TODO: store screenDimensions as a Const vector.
		camera.screenToWorldCoords(screenCoords, Vec2d(canvas.getWidth, canvas.getHeight))
	}
	
	def translateLocal(x: Double, y: Double, m: Mat3x4d, store: outVec3d) = {
		val ray = new Ray(Vec3d(0), Vec3d(x, y, -nearDistance))
		ray.transform(inverse(m))
		ray.translateLocal(store)
		
		store
	}
	
	def waitForRender() = {
		thread.synchronized {
			while (!rendered) thread.wait()
		}
	}
	
	// TODO: rename to loadModelMatrix
	def loadMatrix(model: Mat3x4d) = {
		val modelView = model concatenate camera.view
		matrixToBuffer(modelView, matrixStore)
		matrixStore.flip()
		glLoadMatrix(matrixStore)
	}
	
	private def destroy() = {
		GLDisplay.destroy()
	}
	
	def invokeLater(f: () => Unit) = {
		invokeQueue.add(f)
	}
}

object Renderer {
	private val instance = new ThreadLocal[Renderer]
	
	def apply() = instance.get
	
	def createFrame(width: Int, height: Int, title: String, settings: RenderSettings = RenderSettings.Default) = {
		val r = new Renderer()
		r.settings := settings
		
		val f = new java.awt.Frame
		f.setSize(width, height)
		f.setTitle(title)
		f.setResizable(true)
		f.setLayout(new java.awt.BorderLayout())
		f.addFocusListener(new java.awt.event.FocusAdapter() {
			override def focusGained(e: java.awt.event.FocusEvent) = {
				r.canvas.requestFocus()
			}
		})
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			override def windowClosing(e: java.awt.event.WindowEvent) = {
				r.shutdown()
				f.dispose()
			}
		})

		f.add(java.awt.BorderLayout.CENTER, r.canvas)
		
		f.setVisible(true)
		
		r.start()
		
		r
	}
	
	def createCanvas(width: Int, height: Int, settings: RenderSettings = RenderSettings.Default) = {
		val r = new Renderer()
		r.settings := settings
		
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
}
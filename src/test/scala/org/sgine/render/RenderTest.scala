package org.sgine.render

import scala.io.Source

import java.awt.Canvas
import java.awt.event.WindowEvent
import java.awt.event.WindowAdapter
import java.awt.BorderLayout
import java.awt.Frame
import javax.imageio._

import java.nio._

import org.sgine.core.Color

import org.lwjgl.opengl.{Display => GLDisplay}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL15._
import org.lwjgl.util.glu.GLU._

object RenderTest {
	val f = new Frame
	var keepAlive = true
	var lastRender = 0L
	var elapsed = 0.0
	var frames = 0L
	
	var texture: BasicTexture = _
	var image: Image = _
	
	def main(args: Array[String]): Unit = {
		f.setSize(1024, 768)
		f.setTitle("Test Scala")
		f.setResizable(false)
		f.setLayout(new BorderLayout())
		f.addWindowListener(new WindowAdapter(){
			override def windowClosing(e: WindowEvent): Unit = {
				keepAlive = false
				f.dispose()
			}
		})
		
		val c = new Canvas()
		c.setSize(1024, 768)
		f.add(BorderLayout.CENTER, c)
		
		f.setVisible(true)
		
		GLDisplay.setFullscreen(false)
		GLDisplay.setVSyncEnabled(false)
		GLDisplay.setParent(c)
		GLDisplay.create()
		
		// Setup GL
		glClearDepth(1.0)
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
		glEnable(GL_BLEND)
		glEnable(GL_DEPTH_TEST)
		glDepthFunc(GL_LEQUAL)
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)
		glEnable(GL_TEXTURE_2D)
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
		glViewport(0, 0, 1024, 768)
		glMatrixMode(GL_PROJECTION)
		glLoadIdentity()
		val h = 1024.0f / 768.0f
//		gluPerspective(45.0f, h, 100.0f, 20000.0f)
		glFrustum(-1.0f / h, 1.0f * h, -1.0f, 1.0f, 10.0f, 2000.0f)
		glMatrixMode(GL_MODELVIEW)
		glLoadIdentity()
		
		def toFloatBuffer(args: Float*) = {
			val buffer = org.lwjgl.BufferUtils.createFloatBuffer(args.length)
			
			for (f <- args) {
				buffer.put(f)
			}
			buffer.flip()
			
			buffer
		}
		
		val lightAmbient = toFloatBuffer(0.2f, 0.3f, 0.6f, 1.0f)
		val lightDiffuse = toFloatBuffer(0.2f, 0.3f, 0.6f, 1.0f)
		val matAmbient = toFloatBuffer(0.6f, 0.6f, 0.6f, 1.0f)
		val matDiffuse = toFloatBuffer(0.6f, 0.6f, 0.6f, 1.0f)
		
		glEnable(GL_LIGHTING)
		glEnable(GL_LIGHT0)
		
		glMaterial(GL_FRONT_AND_BACK, GL_AMBIENT, matAmbient)
		glMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE, matDiffuse)
		
		glLight(GL_LIGHT0, GL_AMBIENT, lightAmbient)
		glLight(GL_LIGHT0, GL_DIFFUSE, lightDiffuse)
		
		glEnable(GL_DEPTH_TEST)
		glDepthFunc(GL_LEQUAL)
		
		glEnable(GL_CULL_FACE)
		glShadeModel(GL_SMOOTH)
		
		glEnable(GL_RESCALE_NORMAL)
		
		// Setup
		texture = new BasicTexture(700, 366)
		TextureUtil(texture, ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")), 0, 0, 700, 366)
//		TextureUtil(texture, ImageIO.read(getClass.getClassLoader.getResource("resource/Arial.png")), 0, 0, 256, 256, 256)
		image = Image()
		image.texture = texture
		image.width = 700
		image.height = 366
		
		// Rendering
		while (keepAlive) {
			GLDisplay.update()
			
			if (GLDisplay.isCloseRequested()) {
				keepAlive = false
			}
			
			val currentRender = System.nanoTime()
			val time = (currentRender - lastRender) / 1000000000.0
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
			
			glLoadIdentity()
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
			
			elapsed += time
			frames += 1
			if (elapsed > 1.0) {
				elapsed = 0.0
				System.out.println("FPS: " + frames)
				frames = 0
			}
			
			render()
			
			lastRender = currentRender
		}
		GLDisplay.destroy()
		f.dispose()
		System.exit(0)
	}
	
	def render() = {
		glTranslatef(0.0f, 0.0f, -1000.0f)
		glScalef(0.2f, 0.2f, 0.2f)
		
		image.draw(0.0f, 0.0f)
	}
}
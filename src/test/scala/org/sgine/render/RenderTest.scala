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

import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.util.glu.GLU._

object RenderTest {
	val f = new Frame
	var keepAlive = true
	var lastRender = 0L
	var elapsed = 0.0
	var frames = 0L
	
	var texture: Texture = _
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
		
		Display.setFullscreen(false)
		Display.setVSyncEnabled(false)
		Display.setParent(c)
		Display.create()
		
		// Setup GL
		glClearDepth(1.0)
		glEnable(GL_BLEND)
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST)
		glEnable(GL_TEXTURE_2D)
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA)
		glViewport(0, 0, 1024, 768)
		glMatrixMode(GL_PROJECTION)
		glLoadIdentity()
		val h = 1024.0f / 768.0f
		gluPerspective(45.0f, h, 1.0f, 20000.0f)
		glMatrixMode(GL_MODELVIEW)
		glLoadIdentity()
		
		// Setup
		texture = new Texture(700, 366)
		TextureUtil(texture, ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")), 0, 0, 700, 366)
//		TextureUtil(texture, ImageIO.read(getClass.getClassLoader.getResource("resource/Arial.png")), 0, 0, 256, 256, 256)
		image = Image()
		image.texture = texture
		image.width = 700
		image.height = 366
		
		// Rendering
		while (keepAlive) {
			Display.update()
			
			if (Display.isCloseRequested()) {
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
		Display.destroy()
		f.dispose()
		System.exit(0)
	}
	
	def render() = {
		glTranslatef(0.0f, 0.0f, -1000.0f)
		
		image.draw(0.0f, 0.0f)
	}
}
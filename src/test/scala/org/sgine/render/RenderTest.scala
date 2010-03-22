package org.sgine.render

//import org.newdawn.slick.AngelCodeFont
//import org.newdawn.slick.Font
import scala.io.Source

import java.awt.Canvas
import java.awt.event.WindowEvent
import java.awt.event.WindowAdapter
import java.awt.BorderLayout
import java.awt.Frame
import javax.imageio._

import java.nio._

import org.sgine.core.Color
import org.sgine.render.lwjgl._

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
	
	var texture: LWJGLTexture = _
	var image: LWJGLImage = _
	
	val r = new Renderer
//	var arial: Font = _
	
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
		
		// Set up texture
		val awtImage = ImageIO.read(getClass.getClassLoader.getResource("resource/Arial.png"))
		texture = LWJGLTextureManager(awtImage, 0, 0, 256, 256, true)
		
		// Set up texture coordinates
		LWJGLRenderContext.texCoord(0, 0.0, 1.0)
		LWJGLRenderContext.texCoord(1, 1.0, 1.0)
		LWJGLRenderContext.texCoord(2, 1.0, 0.0)
		LWJGLRenderContext.texCoord(3, 0.0, 0.0)
		
		image = new LWJGLImage(texture, 0.0, 0.0, 256.0, 256.0)
		
		r.matrix.translateZ(-100.0)
		r.matrix.rotZ(0.2)
		
//		arial = new AngelCodeFont("Arial", getClass.getClassLoader.getResourceAsStream("resource/Arial.fnt"), getClass.getClassLoader.getResourceAsStream("resource/Arial.png"))
//		r.g .setFont(arial)
//		r.g.setFont(new AngelCodeFont("org/newdawn/slick/data/defaultfont.fnt", "org/newdawn/slick/data/defaultfont.png"))
		
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
		LWJGLRenderContext.translate(0.0, 0.0, -100.0)
//		
		LWJGLRenderContext.setTexture(texture)
		LWJGLRenderContext.drawRect(-50.0, -50.0, 32.0, 32.0)
//		LWJGLRenderContext.setTexture(null)
//		LWJGLRenderContext.setColor(blue = 1.0, alpha = 0.5)
//		LWJGLRenderContext.drawRect(0.0, 0.0, 10.0, 10.0)
//		
//		LWJGLRenderContext.setColor(0.8, 0.8, 0.8, 0.8)
//		LWJGLRenderContext.drawImage(image, 0.0, -40.0, 35.0, 18.0)
//		
		LWJGLRenderContext.flush()
		
//		r.color = Color.Red
//		r.g.drawOval(0.0f, 0.0f, 5.0f, 5.0f)
		r.color = Color.Green
		r.drawRect(0.0f, -30.0f, 10.0f, 10.0f)
		
//		r.g.drawString("Testing", 0.0f, 0.0f)
	}
}
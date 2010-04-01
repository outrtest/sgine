package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio.ImageIO

import org.lwjgl.opengl.GL11._

import scala.io.Source

object TestFonts {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Fonts")
		
		val m = Matrix4().translate(x = -70.0, z = -1000.0)
		val fps = FPS(1.0)
		
		val font = AngelCodeFont(Source.fromURL(getClass.getClassLoader.getResource("resource/Arial.fnt")), getClass.getClassLoader.getResource("resource/Arial.png"))
		
		val a = new Array[() => Unit](3)
		a(0) = MatrixState(m)
		a(1) = () => font.drawString("Hello World!")
		a(2) = fps
		r.renderable := RenderList(a)
		
		println("Renderer started! " + 81.asInstanceOf[Char] + " - " + font.face + ", " + font.size)
	}
}
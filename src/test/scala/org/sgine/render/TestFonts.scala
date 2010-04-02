package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio.ImageIO

import org.lwjgl.opengl.GL11._

import scala.io.Source

object TestFonts {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Fonts")
		
		val m1 = Matrix4().translate(x = -200.0, z = -1000.0)
		val m2 = Matrix4().translate(x = -200.0, y = -25.0, z = -1000.0)
		val fps = FPS(1.0)
		
		val font = AngelCodeFont(Source.fromURL(getClass.getClassLoader.getResource("resource/Arial.fnt")), getClass.getClassLoader.getResource("resource/Arial.png"))
		
		val a = new Array[() => Unit](5)
		a(0) = MatrixState(m1)
		a(1) = () => font.drawString("VAST Kerning Example Enabled", true)
		a(2) = MatrixState(m2)
		a(3) = () => font.drawString("VAST Kerning Example Disabled", false)
		a(4) = fps
		r.renderable := RenderList(a)
	}
}
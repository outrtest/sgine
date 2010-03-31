package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio.ImageIO

import org.lwjgl.opengl.GL11._

import scala.io.Source

object TestFonts {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Fonts")
		
		val m = Matrix4().translate(z = -1000.0)
		val fps = FPS(1.0)
		
		val font = AngelCodeFont(Source.fromURL(getClass.getClassLoader.getResource("resource/Arial.fnt")), getClass.getClassLoader.getResource("resource/Arial.png"))
		
		val a = new Array[() => Unit](13)
		a(0) = MatrixState(m)
		a(1) = () => font('H').draw(-60.0)
		a(2) = () => font('e').draw(-40.0)
		a(3) = () => font('l').draw(-20.0)
		a(4) = () => font('l').draw(0.0)
		a(5) = () => font('o').draw(20.0)
		a(6) = () => font(' ').draw(40.0)
		a(7) = () => font('W').draw(60.0)
		a(8) = () => font('o').draw(80.0)
		a(9) = () => font('r').draw(100.0)
		a(10) = () => font('l').draw(120.0)
		a(11) = () => font('d').draw(140.0)
		a(12) = fps
		r.renderable := RenderList(a)
		
		println("Renderer started! " + 81.asInstanceOf[Char] + " - " + font.face + ", " + font.size)
	}
}
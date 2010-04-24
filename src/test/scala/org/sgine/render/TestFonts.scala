package org.sgine.render

import org.sgine.event._

import org.sgine.input.Keyboard
import org.sgine.input.event.KeyEvent

import org.sgine.math.Matrix4

import org.sgine.render.font.BitmapFont

import org.sgine.work._

import javax.imageio.ImageIO

import org.lwjgl.opengl.GL11._

import scala.io.Source

object TestFonts {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Fonts")
		r.verticalSync := true

		val franklinFont = BitmapFont("Franklin")
		val arialFont = BitmapFont("Arial")
		val lcdFont = BitmapFont("lcd")
		
		val m1 = Matrix4().translate(z = -500.0)
		val m2 = Matrix4().translate(y = -50.0, z = -500.0)
		val fps = FPS(1.0, lcdFont)
		
		val a = new Array[() => Unit](5)
		a(0) = MatrixState(m1)
		a(1) = () => franklinFont.drawString("Franklin Gothic Heavy with a blue gradient.", true)
		a(2) = MatrixState(m2)
		a(3) = () => arialFont.drawString("Standard Arial", false)
		a(4) = fps
		r.renderable := RenderList(a)
		
		Keyboard.listeners += test _
		
		println(franklinFont.measureWidth("Franklin Gothic Heavy with a blue gradient.", true))
	}
	
	private def test(evt: KeyEvent) ={
		println("test: " + evt)
	}
}
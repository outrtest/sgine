package org.sgine.render

import org.sgine.event._

import org.sgine.input.Keyboard
import org.sgine.input.event.KeyEvent

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

import org.sgine.log._

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
		val franklinSmaller = franklinFont.derive(30.0)
		val arialFont = BitmapFont("Arial")
		val lcdFont = BitmapFont("lcd")
		
		val m1 = Mat3x4 scale(0.5) translate(Vec3(0.0, 0.0, -500.0))
		val m2 = Mat3x4 scale(0.5) translate(Vec3(0.0, 50.0, -500.0))
		val m3 = Mat3x4 scale(0.5) translate(Vec3(0.0, -50.0, -500.0))
		val fps = FPS(1.0, lcdFont)
		
		r.renderable := RenderList(
									MatrixState(m1),
									() => franklinFont.drawString("Franklin Gothic Heavy with a blue gradient.", true),
									MatrixState(m2),
									() => arialFont.drawString("Standard Arial", false),
									MatrixState(m3),
									() => franklinSmaller.drawString("Derived version of Franklin Gothic Heavy.", true),
									fps
								   )
		
		Keyboard.listeners += test _
		
		info(franklinFont.measureWidth("Franklin Gothic Heavy with a blue gradient.", true))
	}
	
	private def test(evt: KeyEvent): Unit = {
		info("test: " + evt)
	}
}
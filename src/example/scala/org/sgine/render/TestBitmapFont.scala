package org.sgine.render

import org.sgine.core.Resource

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

import org.sgine.log._

import org.sgine.render.font._

import org.sgine.work._

object TestBitmapFont {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test BitmapFont")
		r.verticalSync := false

		val arialFont = BitmapFontLoader.load(Resource("Arial64.fnt"))
		
		val m = Mat3x4 scale(0.5) translate(Vec3(0.0, 0.0, -500.0))
		
		r.renderable := RenderList(
									MatrixState(m),
									() => arialFont.drawString("Standard Arial", false)
								   )
	}
}
package org.sgine.render

import org.lwjgl._
import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.log._

import org.sgine.render.shape._

import simplex3d.math._
import simplex3d.math.doublem.renamed._

object TestShape2 {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Shape 2", RenderSettings.High)
		r.verticalSync := false
		
		// Setup lighting
//		r.light0.enabled := true
//		r.light0.ambience := Color(0.2, 0.3, 0.6, 1.0)
//		r.light0.diffuse := Color(0.2, 0.3, 0.6, 1.0)

		val m = Mat3x4 translate(Vec3(0, 0, -2000.0))
		val s = Shape()
		
		val trans = new java.awt.geom.AffineTransform()
	    trans.scale(1, -1)
		val font = new java.awt.Font("Dialog", java.awt.Font.BOLD, 150).deriveFont(trans)
	    val fontRenderContext = new java.awt.font.FontRenderContext(null, true, true)
	    val glyphVector = font.createGlyphVector(fontRenderContext, "Sgine!")
		val shape = glyphVector.getOutline
		val rect = new java.awt.geom.Rectangle2D.Double(0.0, 0.0, 5.0, 5.0)
		val data = ShapeUtilities.convert(shape)
		info("Data: " + data.length)
		s(data)
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(m), s, fps)
	}
}
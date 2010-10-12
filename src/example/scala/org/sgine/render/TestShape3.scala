package org.sgine.render

import org.lwjgl._
import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.log._

import org.sgine.render.font._
import org.sgine.render.shape._

import simplex3d.math._
import simplex3d.math.doublem.renamed._

object TestShape3 {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Shape 3", RenderSettings.High)
		r.verticalSync := false
		
		val m = Mat3x4 translate(Vec3(0, 0, -1000.0))
		val s = MutableShape()
		val franklinFont = BitmapFont("Franklin")
		franklinFont(s, "Now is the time for all good men to come to the aid of their country.", true, 400.0, textAlignment = HorizontalAlignment.Right)
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(m), () => franklinFont.texture.bind(), s, fps)
	}
}
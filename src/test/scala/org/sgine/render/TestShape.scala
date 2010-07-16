package org.sgine.render

import org.lwjgl._
import org.lwjgl.opengl._
import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.shape._

import simplex3d.math._
import simplex3d.math.doublem.renamed._

object TestSpatial {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Spatial")
		r.verticalSync := false
		
		// Setup lighting
		r.light0.enabled := true
		r.light0.ambience := Color(0.2, 0.3, 0.6, 1.0)
		r.light0.diffuse := Color(0.2, 0.3, 0.6, 1.0)

		val m = Mat3x4 translate(Vec3(0, 0, -200.0))
		val s = Shape()
		val data = new ShapeData() {
			val mode = GL_TRIANGLES
			val cull = Face.Front
			val material = Material.AmbientAndDiffuse
			val length = 3
			def color(index: Int) = colors(index)
			def vertex(index: Int) = vertices(index)
			def texture(index: Int) = null
			def normal(index: Int) = null
			val hasColor = true
			val hasTexture = false
			val hasNormal = false
			
			val vertices = List(Vec3(1.0, -1.0, 0.0), Vec3(-1.0, -1.0, 0.0), Vec3(0.0, 1.0, 0.0))
			val colors = List(Color(255, 50, 160, 255), Color(180, 255, 70, 255), Color(90, 10, 255, 255))
		}
		s(data)
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(m), s, fps)
	}
}
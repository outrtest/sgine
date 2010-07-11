package org.sgine.render

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.spatial._

import simplex3d.math._
import simplex3d.math.doublem.renamed._

object TestSpatial {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Spatial")
		r.verticalSync := false
		
		val m = Mat3x4 translate(Vec3(0, 0, -200.0))
		val s = new Spatial() {
		}
		val data = new MeshData() {
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
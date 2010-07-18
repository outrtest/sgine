package org.sgine.render

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Mesh

object TestMesh {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Mesh")
		r.verticalSync := false
		
		val m = Mat3x4 translate(Vec3(0, 0, -800.0))
		val mesh = new Mesh()
		mesh.indexes = List(0, 1, 2)
		mesh.vertices = List(
							 100.0, 100.0, 0.0,
							 -100.0, 100.0, 0.0,
							 0.0, -100.0, 0.0)
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(m), mesh, fps)
	}
}
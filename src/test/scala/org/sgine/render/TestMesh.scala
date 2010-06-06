package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Mesh

object TestMesh {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Mesh")
		r.verticalSync := false
		
		val m = Matrix4().translate(z = -1000.0).scaleAll(0.04)
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
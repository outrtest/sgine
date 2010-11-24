package org.sgine.render

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core._

import org.sgine.process._

import org.sgine.render.primitive.Ellipsoid

object TestEllipsoid {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Ellipsoid", RenderSettings.High)
		r.verticalSync := false

		val t = TextureManager(Resource("puppies.jpg"))

		val m = Mat3x4 translate(Vec3(0, 0, -800.0))
		val i = RenderImage(t)
		val ellipsoid = Ellipsoid(300.0, 300.0, 100.0, 60, 60, Color.White, i)
		val fps = FPS(1.0)

		r.renderable := RenderList(MatrixState(m), ellipsoid)

		update(0.005) {
			m := Mat3x4 rotateX(0.005) rotateY(0.005) concatenate(m)
		}
	}
}
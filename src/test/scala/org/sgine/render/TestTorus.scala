package org.sgine.render

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Torus

object TestTorus {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Torus", RenderSettings.High)
		r.verticalSync := false

		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))

		val m = Mat3x4 scale(0.04) translate(Vec3(0, 0, -1000.0))
		val i = RenderImage(t)
		val torus = Torus(200.0, 50.0, 150, 6, Color.White, i)
		val fps = FPS(1.0)

		r.renderable := RenderList(MatrixState(m), torus, fps)
		
		while(true) {
			Thread.sleep(5)
			m := Mat3x4 rotateX(0.005) rotateY(0.005) concatenate(m)
		}
	}
}
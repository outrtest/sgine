package org.sgine.render

import org.sgine.math._

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Torus

object TestTorus {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Torus", 4, 8, 4, 4)
		r.verticalSync := false

		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))

		val m = Matrix4(mutability = Mutability.Mutable, storeType = StoreType.DirectBuffer).translate(z = -1000.0).scaleAll(0.04)
		println(m)
		val i = RenderImage(t)
		val torus = Torus(200.0, 50.0, 150, 6, Color.White, i)
		val fps = FPS(1.0)

		r.renderable := RenderList(MatrixState(m), torus, fps)
		
		while(true) {
			Thread.sleep(5)
			m.rotate(0.005, 0.005, 0.005)
		}
	}
}
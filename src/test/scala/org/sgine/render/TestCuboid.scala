package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Cuboid

object TestCuboid {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Cuboid", 4, 8, 4, 4)
		r.verticalSync := false

		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))

		val m = Matrix4().translate(z = -1000.0).scaleAll(0.04)
		val i = Image(t)
		val cuboid = Cuboid(300.0, 100.0, 200.0, Color.White, i)
		val fps = FPS(1.0)

		r.renderable := RenderList(MatrixState(m), cuboid, fps)

		while(true) {
			Thread.sleep(5)
			m.rotate(0.005, 0.005, 0.0)
		}
	}
}
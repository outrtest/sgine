package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.primitive.Disk

object TestDisk {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Quad")
		r.verticalSync := false

		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))

		val m = Matrix4().translate(z = -1000.0).scaleAll(0.04)
		val i = Image(t)
		val disk = Disk(300.0, 200.0, 15, Color.Blue, i)
		val fps = FPS(1.0)

		r.renderable := RenderList(MatrixState(m), disk, fps)
	}
}
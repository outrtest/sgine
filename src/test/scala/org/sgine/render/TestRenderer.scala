package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

object TestRenderer {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Renderer")
		
		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))
		
		val m = Matrix4().translate(z = -1000.0).scaleAll(0.04)
		val i = Image(t)
		val fps = FPS(1.0)
		
		val a = new Array[() => Unit](3)
		a(0) = MatrixState(m)
		a(1) = i
		a(2) = fps
		r.renderable := RenderList(MatrixState(m), i, fps)
		
		println("Renderer started!")
		while (r.isAlive) {
			m.rotate(y = 0.001)
			
			Thread.sleep(1)
		}
	}
}
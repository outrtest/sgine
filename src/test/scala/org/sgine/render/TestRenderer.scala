package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

object TestRenderer {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Renderer")
		
		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))
		
		val m = Matrix4().translate(z = -1000.0)
		val i = Image(t)
		val fps = FPS(1.0)
		
		val l = MatrixState(m) :: i :: fps :: Nil
		r.renderable := RenderList(l)
		
		println("Renderer started!")
		while (r.isAlive) {
			m.rotate(y = 0.001)
			
			Thread.sleep(1)
		}
	}
}
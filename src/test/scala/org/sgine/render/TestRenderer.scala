package org.sgine.render

import org.sgine.math.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

object TestRenderer {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Renderer")
		
		val t = new Texture(700, 366)
		TextureUtil(t, ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")), 0, 0, 700, 366)
		
		val m = Matrix4().translate(z = -1000.0)
		val i = Image(t)
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(m) :: i :: fps :: Nil)
		
		println("Renderer started!")
	}
}
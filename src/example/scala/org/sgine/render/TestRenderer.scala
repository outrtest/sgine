package org.sgine.render

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.log._

object TestRenderer {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Renderer", RenderSettings.High)
		
		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))
		
		val m = Mat3x4 translate(Vec3(0, 0, -800.0))
		val i = RenderImage(t)
		val fps = FPS(1.0)
		
		val a = new Array[() => Unit](3)
		a(0) = MatrixState(m)
		a(1) = i
		a(2) = fps
		r.renderable := RenderList(MatrixState(m), i, fps)
		
		info("Renderer started!")
		while (r.isAlive) {
			m := Mat3x4 rotateY(0.001) concatenate(m)
			
			Thread.sleep(1)
		}
	}
}
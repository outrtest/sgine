package org.sgine.render

import org.sgine.math.mutable.Matrix4

import javax.imageio._

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

import org.sgine.render.awt.ShapeRenderableGL
import org.sgine.render.awt.ShapeRenderableTexture

object TestShape {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test Shape")
		r.verticalSync := false
		
		val t = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))
		
		val m = Matrix4().translate(z = -1000.0).scaleAll(0.04)
		val shape = new java.awt.geom.Rectangle2D.Double(50.0, 50.0, 100.0, 100.0)
		val shapeRenderable = new ShapeRenderableTexture(shape)
		val shapeRenderable2 = new ShapeRenderableGL(shape)
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(m), shapeRenderable, shapeRenderable2, fps)
	}
}
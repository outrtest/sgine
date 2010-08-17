package org.sgine.render

import javax.imageio.ImageIO

import simplex3d.math.doublem.renamed._

object TestTexturedQuad {
	def main(args: Array[String]): Unit = {
		val r = Renderer.createFrame(1024, 768, "Test TexturedQuad")
		r.verticalSync := false
		
		val matrix = Mat3x4.translate(Vec3(0, 0, -800.0))
		
		val texture = TextureUtil(ImageIO.read(getClass.getClassLoader.getResource("resource/puppies.jpg")))
		val quad = new TexturedQuad()
		quad.texture := texture
		quad.width := texture.width
		quad.height := texture.height
		
		val fps = FPS(1.0)
		
		r.renderable := RenderList(MatrixState(matrix), quad, fps)
	}
}
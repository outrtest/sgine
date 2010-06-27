package org.sgine.render.primitive

import org.lwjgl.opengl.GL11._

import org.sgine.core.Color

class Mesh(val color: Color = Color.White) extends Primitive {
	var indexes: Seq[Int] = Nil
	var vertices: Seq[Double] = Nil
	val mode = GL_TRIANGLES
	def vertexCount = indexes.length
	def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)
	def texture(index: Int) = {
	}
	def vertex(index: Int) = {
		val i = 3 * indexes(index)
		glVertex3d(vertices(i), vertices(i + 1), vertices(i + 2))
	}
}
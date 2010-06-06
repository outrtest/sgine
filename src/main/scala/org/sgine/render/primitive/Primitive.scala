package org.sgine.render.primitive

import org.lwjgl.opengl.GL11._

trait Primitive extends Function0[Unit] {
	def mode: Int
	def vertexCount: Int
	def color(index: Int): Unit
	def texture(index: Int): Unit
	def vertex(index: Int): Unit
	
	private var range: Range = _
	
	def apply() = {
		if ((range == null) || (range.length != vertexCount)) {
			range = 0 until vertexCount
		}
		
		begin()
		range.foreach(renderVertex)
		end()
	}
	
	protected def begin() = {
		glBegin(mode)
	}
	
	protected def end() = {
		glEnd()
	}
	
	private val renderVertex = (index: Int) => {
		color(index)
		texture(index)
		vertex(index)
	}
}
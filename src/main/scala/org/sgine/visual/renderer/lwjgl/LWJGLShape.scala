package org.sgine.visual.renderer.lwjgl

import org.sgine.math._

import org.sgine.visual.Shape

import org.lwjgl.opengl.GL11._

class LWJGLShape private (val shape: Shape, points: Vector3*) extends org.sgine.opengl.shape.Shape(GL_TRIANGLES, points: _*) {
	override def apply(time:Double) = {
		// TODO: use pigments and such
		
		super.apply(time)
	}
}

object LWJGLShape {
	def apply(shape: Shape) = new LWJGLShape(shape, shape.mesh().vertices:_*)
}
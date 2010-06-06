package org.sgine.render

import org.sgine.math.Matrix4

import org.lwjgl.opengl.GL11._

case class MatrixState(m: Matrix4) extends Function0[Unit] {
	def apply() = glLoadMatrix(m.buffer)
}
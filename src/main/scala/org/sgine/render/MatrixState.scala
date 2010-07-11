package org.sgine.render

import org.lwjgl.opengl.GL11._

import simplex3d.math._
import simplex3d.math.doublem._

case class MatrixState(m: Mat3x4d) extends Function0[Unit] {
	def apply() = Renderer.instance.get.loadMatrix(m)
}
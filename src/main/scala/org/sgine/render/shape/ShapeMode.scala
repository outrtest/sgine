package org.sgine.render.shape

import org.sgine.core.Enum
import org.sgine.core.Enumerated

import org.lwjgl.opengl.GL11._

sealed class ShapeMode(val value: Int) extends Enum

object ShapeMode extends Enumerated[ShapeMode] {
	case object Quads extends ShapeMode(GL_QUADS)
	case object Triangles extends ShapeMode(GL_TRIANGLES)
	case object TriangleFan extends ShapeMode(GL_TRIANGLE_FAN)
	
	ShapeMode(Quads, Triangles, TriangleFan)
}
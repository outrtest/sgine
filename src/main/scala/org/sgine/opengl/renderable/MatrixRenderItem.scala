package org.sgine.opengl.renderable

import org.sgine.math.Matrix4
import org.sgine.math.Vector3
import org.lwjgl.opengl.GL11._;

class MatrixRenderItem(private var _matrix: Matrix4) extends RenderItem {
	def matrix = _matrix
	def matrix_=(_matrix: Matrix4) = {
		this._matrix = _matrix
		matrix.getTranspose(buffer, false)
	}
	
	private val buffer = matrix.getTranspose(null, false)
	
	def begin(renderable: Renderable, time: Double) = glLoadMatrix(buffer)
	
	def vertex(renderable: Renderable, time: Double, index: Int, vertex: Vector3) = {}
	
	def end(renderable: Renderable, time: Double) = {}
}
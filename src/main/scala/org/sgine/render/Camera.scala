package org.sgine.render

import java.nio.ByteBuffer
import java.nio.ByteOrder

import simplex3d.math._
import simplex3d.math.doublem._

class Camera {
	var matrix = Mat3x4d.Identity
	
	val buffer = ByteBuffer.allocateDirect(128).order(ByteOrder.nativeOrder).asDoubleBuffer
	
	validate()
	
	def validate() = {
		matrixToBuffer(matrix, buffer)
		buffer.flip()
	}
}
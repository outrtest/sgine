package org.sgine.math

import java.nio._
import simplex3d.math.doublem._
import simplex3d.math.doublem.DoubleMath._

object MathUtil {
	def eulerMat(x: Double, y: Double, z: Double) :Mat3d = {
		val cx = cos(x)
		val sx = sin(x)
		val cy = cos(-y)
		val sy = sin(-y)
		val cz = cos(z)
		val sz = sin(z)
		
		val cxsy = cx * sy
		val sxsy = sx * sy
		
		val d00 = cy * cz
		val d01 = -cy * sz
		val d02 = -sy
		
		val d10 = -sxsy * cz + cx * sz
		val d11 = sxsy * sz + cx * cz
		val d12 = -sx * cy
		
		val d20 = cxsy * cz + sx * sz
		val d21 = -cxsy * sz + sx * cz
		val d22 = cx * cy
		
		Mat3d(
			d00, d10, d20,
			d01, d11, d21,
			d02, d12, d22
		)
	}
	
	def modelViewBuffer(model: Mat3x4d, view: Mat3x4d, buffer: DoubleBuffer) {
		
	}
}
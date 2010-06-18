package org.sgine.scene.ext

trait ResolutionNode extends WorldMatrixNode {
	def setResolution(width: Double, height: Double) = {
		worldMatrix().identity()
		worldMatrix().translate(z = -101.0)
		worldMatrix().scaleAll(2.02 / height)
	}
}
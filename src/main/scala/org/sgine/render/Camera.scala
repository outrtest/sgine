// This file is under BSD license.

package org.sgine.render

import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._

/**
 *
 * @author Aleksey Nikiforov (lex)
 */
class Camera(
	val view: Mat3x4 = inverseTransformation(Vec3.One, Mat3.Identity, Vec3.Zero),
	val projection: Mat4 = orthoProj(-100, 100, -100, 100, 5, 1000)
) {
	private val viewProjection = Mat4(0)
	private val inverseViewProjection = Mat4(0)

	def update() {
		viewProjection := projection*Mat4(view)
		inverseViewProjection := inverse(viewProjection)
	}

	def screenToWorldCoords(screenCoords: inVec3) = {
		val normalizedScreenCoords = Vec4(
			screenCoords.x,
      		-screenCoords.y,
      		screenCoords.z,
      		1
		)
    	val worldCoords = inverseViewProjection*normalizedScreenCoords
    	worldCoords.xyz/worldCoords.w
	}

	def worldToScreenCoords(worldCoords: inVec3) = {
		val transformed = viewProjection*Vec4(worldCoords, 1)
		val normalizedScreenCoords = transformed.xyz/transformed.w
		Vec3(
			normalizedScreenCoords.x,
			-normalizedScreenCoords.y,
			normalizedScreenCoords.z
		)
	}
}
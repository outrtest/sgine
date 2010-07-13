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

  // TODO: Change to use properties and auto-update
  def update() {
    viewProjection := projection*Mat4(view)
    inverseViewProjection := inverse(viewProjection)
  }

  def screenToWorldCoords(screenCoords: inVec3, screenDimensions: inVec2) = {
    val normalizedScreenCoords = Vec4(
      2*screenCoords.x/screenDimensions.x - 1,
      2*screenCoords.y/screenDimensions.y - 1,
      screenCoords.z,
      1
    )
    val worldCoords = inverseViewProjection*normalizedScreenCoords
    worldCoords.xyz/worldCoords.w
  }

  def worldToScreenCoords(worldCoords: inVec3, screenDimensions: inVec2) = {
    val halfScreen = screenDimensions/2
    val transformed = viewProjection*Vec4(worldCoords, 1)
    val normalizedScreenCoords = transformed.xyz/transformed.w
    Vec3(
      normalizedScreenCoords.x*halfScreen.x + halfScreen.x,
      normalizedScreenCoords.y*halfScreen.y + halfScreen.y,
      normalizedScreenCoords.z
    )
  }
}

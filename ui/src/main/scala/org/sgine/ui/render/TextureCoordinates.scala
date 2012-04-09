package org.sgine.ui.render

/**
 * TextureCoordinates is a convenience class for generating common scenarios of texture coordinates.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object TextureCoordinates {
  /**
   * Creates texture coordinates for the section specified of a texture based on the dimensions of
   * the texture supplied.
   */
  def rectCoords(x: Double, y: Double, width: Double, height: Double, textureWidth: Double,
      textureHeight: Double) = {
    val left = x / textureWidth
    val right = (x + width) / textureWidth
    val top = y / textureHeight
    val bottom = (y + height) / textureHeight
    List(
      left, top,
      right, top,
      left, bottom,
      right, bottom,
      left, bottom,
      right, top
    )
  }

  /**
   * Generic generation of texture coordinates presuming use of the entire texture.
   */
  def rect(flipHorizontal: Boolean = false, flipVertical: Boolean = false) = {
    val left = if (flipHorizontal) {
      1.0
    } else {
      0.0
    }
    val right = if (flipHorizontal) {
      0.0
    } else {
      1.0
    }
    val top = if (flipVertical) {
      1.0
    } else {
      0.0
    }
    val bottom = if (flipVertical) {
      0.0
    } else {
      1.0
    }
    List(
      left, top,
      right, top,
      left, bottom,
      right, bottom,
      left, bottom,
      right, top
    )
  }

  /**
   * Generic generation of texture coordinates presuming use of entire texture.
   */
  def box() = {
    rect() ::: rect() ::: rect() ::: rect()
  }
}
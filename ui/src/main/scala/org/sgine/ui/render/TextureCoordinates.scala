package org.sgine.ui.render

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object TextureCoordinates {
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

  def rect(flipHorizontal: Boolean = false, flipVertical: Boolean = false) = {
    val left = if (flipHorizontal) {
      1.0
    }
    else {
      0.0
    }
    val right = if (flipHorizontal) {
      0.0
    }
    else {
      1.0
    }
    val top = if (flipVertical) {
      1.0
    }
    else {
      0.0
    }
    val bottom = if (flipVertical) {
      0.0
    }
    else {
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
}
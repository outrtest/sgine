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
  def rectCoords(x: Double, y: Double, width: Double, height: Double, textureWidth: Double, textureHeight: Double) = {
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

  def slice(x1: Double, y1: Double, x2: Double, y2: Double, width: Double, height: Double) = {
    rectCoords(x1, y1, x2 - x1, y2 - y1, width, height)
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
    rect() :::          // Front
    rect(true) :::      // Back
    rect() :::          // Left
    rect(true) :::      // Right
    rect() :::          // Top
    rect(false, true)   // Bottom
  }

  def scale9(x1: Double, y1: Double, x2: Double, y2: Double, width: Double, height: Double) = {
    slice(0.0, 0.0, x1, y1, width, height) :::          // Top-Left
    slice(x1, 0.0, x2, y1, width, height) :::           // Top
    slice(x2, 0.0, width, y1, width, height) :::        // Top-Right
    slice(0.0, y1, x1, y2, width, height) :::           // Left
    slice(x1, y1, x2, y2, width, height) :::            // Center
    slice(x2, y1, width, y2, width, height) :::         // Right
    slice(0.0, y2, x1, height, width, height) :::       // Bottom-Left
    slice(x1, y2, x2, height, width, height) :::        // Bottom
    slice(x2, y2, width, height, width, height)         // Bottom-Right
  }
}
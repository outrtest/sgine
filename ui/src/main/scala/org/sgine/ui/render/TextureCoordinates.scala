package org.sgine.ui.render

import com.badlogic.gdx.graphics.Texture

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object TextureCoordinates {
  def rectCoords(x: Double, y: Double, width: Double, height: Double, texture: Texture) = {
    val left = x / texture.getWidth
    val right = (x + width) / texture.getWidth
    val top = y / texture.getHeight
    val bottom = (y + height) / texture.getHeight
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
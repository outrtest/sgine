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
}
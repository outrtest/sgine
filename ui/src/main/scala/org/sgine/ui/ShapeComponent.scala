package org.sgine.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import render.ArrayBuffer

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ShapeComponent extends RenderableComponent {
  private val arrayBuffer = new ArrayBuffer(false)

  protected def draw(batch: SpriteBatch) = {
  }
}
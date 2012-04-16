package org.sgine.ui

import org.sgine.Resource
import org.sgine.property.NumericProperty
import render.{Vertex, TextureCoordinates}
import com.badlogic.gdx.graphics.Texture

/**
 * Displays Scale-9 images for rendering at different sizes.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Scale9 extends ShapeComponent {
  def this(resource: Resource, x1: Double, y1: Double, x2: Double, y2: Double) = {
    this()
    load(resource, x1, y1, x2, y2)
  }

  def texture = _texture

  /**
   * Defines slice points for the Scale-9 texture.
   */
  object slice extends ComponentPropertyParent(this) {
    val x1 = NumericProperty(0.0)
    val y1 = NumericProperty(0.0)
    val x2 = NumericProperty(0.0)
    val y2 = NumericProperty(0.0)

    def set(x1: Double, y1: Double, x2: Double, y2: Double) = {
      this.x1 := x1
      this.y1 := y1
      this.x2 := x2
      this.y2 := y2
    }
  }

  texture.onChange {
    val texture = this.texture()
    if (texture != null) {
      measured.width := texture.getWidth
      measured.height := texture.getHeight
    }
  }

  onUpdate(texture,
    size.width,
    size.height,
    slice.x1,
    slice.y1,
    slice.x2,
    slice.y2) {
    val texture = this.texture()
    if (texture != null) {
      _textureCoordinates := TextureCoordinates.scale9(slice.x1(), slice.y1(), slice.x2(), slice.y2(), texture.getWidth, texture.getHeight)
      _vertices := Vertex.scale9(slice.x1(), slice.y1(), slice.x2(), slice.y2(), 0.0, texture.getWidth, texture.getHeight, size.width(), size.height())
    }
  }

  /**
   * Loads the resource supplied as a Texture and slices as a Scale-9.
   */
  def load(resource: Resource, x1: Double, y1: Double, x2: Double, y2: Double) = {
    texture := new Texture(resource)

    slice.set(x1, y1, x2, y2)
  }
}

object Scale9 {
  def apply(resource: Resource, x1: Double, y1: Double, x2: Double, y2: Double) = new Scale9(resource, x1, y1, x2, y2)
}
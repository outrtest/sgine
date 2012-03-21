package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.property._
import org.sgine._
import render.{TextureCoordinates, Vertex}

/**
 * Image represents a visual rectangular texture region.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image extends ShapeComponent {
  def texture = _texture

  /**
   * This defines the region of the texture to be displayed. This is reset to the bounds of the
   * texture when the texture property is changed.
   */
  object textureRegion extends PropertyParent {
    val x = Property[Int](0)
    val y = Property[Int](0)
    val width = Property[Int](0)
    val height = Property[Int](0)
  }

  texture.onChange {
    val texture = this.texture()
    if (texture != null) {
      measured.width := texture.getWidth
      measured.height := texture.getHeight
      textureRegion.x := 0
      textureRegion.y := 0
      textureRegion.width := texture.getWidth
      textureRegion.height := texture.getHeight
    }
  }

  onUpdate(texture,
    textureRegion.x,
    textureRegion.y,
    textureRegion.width,
    textureRegion.height,
    size.width,
    size.height) {
    val texture = this.texture()
    if (texture != null) {
      _textureCoordinates := TextureCoordinates.rectCoords(
        textureRegion.x(),
        textureRegion.y(),
        textureRegion.width(),
        textureRegion.height(),
        texture.getWidth,
        texture.getHeight
      )
      _vertices := Vertex.rect(size.width(), size.height())
    }
  }

  /**
   * Loads the resource suppled as a Texture for this Image.
   */
  def load(resource: Resource) = {
    texture := new Texture(resource)
  }
}

object Image {
  /**
   * Convenience method to create an Image from a resource path.
   */
  def apply(resource: String): Image = apply(Resource(resource))

  /**
   * Convenience method to create an Image from a Resource.
   */
  def apply(resource: Resource): Image = {
    val image = new Image()
    image.load(resource)
    image
  }
}
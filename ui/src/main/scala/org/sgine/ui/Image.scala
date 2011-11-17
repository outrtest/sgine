package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.property._
import org.sgine._
import render.{TextureCoordinates, Vertex, ArrayBuffer}

/**
 * Image represents a visual rectangular texture region.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image extends RenderableComponent {
  // TODO: extend ShapeComponent instead of RenderableComponent
  private val arrayBuffer = new ArrayBuffer(false)

  /**
   * The texture to be used for this Image.
   */
  val texture = Property[Texture]()

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
    size.width := texture().getWidth
    size.height := texture().getHeight
    textureRegion.x := 0
    textureRegion.y := 0
    textureRegion.width := texture().getWidth
    textureRegion.height := texture().getHeight
  }

  onUpdate(texture,
    textureRegion.x,
    textureRegion.y,
    textureRegion.width,
    textureRegion.height,
    size.width,
    size.height) {
    updateArrayBuffer()
  }

  private def updateArrayBuffer() = {
    val vertices = Vertex.rect(size.width(), size.height())
    val textureCoordinates = TextureCoordinates
        .rectCoords(textureRegion.x(), textureRegion.y(), textureRegion.width(),
      textureRegion.height(), texture.getWidth, texture.getHeight)
    arrayBuffer.data = vertices ::: textureCoordinates
  }

  /**
   * Loads the resource suppled as a Texture for this Image.
   */
  def load(resource: Resource) = {
    texture := new Texture(resource)
  }

  protected def draw() = {
    val texture = this.texture()
    if (texture != null) {
      texture.bind()
      arrayBuffer.bind()
      arrayBuffer.bindTextureCoordinates(18)
      arrayBuffer.drawVertices(0, 9)
    }
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
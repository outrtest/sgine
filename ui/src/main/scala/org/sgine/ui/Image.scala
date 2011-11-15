package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.property._
import org.sgine._
import render.{TextureCoordinates, Vertex, ArrayBuffer}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image extends RenderableComponent {
  // TODO: extend ShapeComponent instead of RenderableComponent
  private val arrayBuffer = new ArrayBuffer(false)

  val texture = Property[Texture]()

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
  def apply(resource: String): Image = apply(Resource(resource))

  def apply(resource: Resource): Image = {
    val image = new Image()
    image.load(resource)
    image
  }
}
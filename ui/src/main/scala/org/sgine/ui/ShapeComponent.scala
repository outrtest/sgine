package org.sgine.ui

import render.ArrayBuffer
import org.sgine.property.Property
import com.badlogic.gdx.graphics.Texture

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ShapeComponent extends RenderableComponent {
  private val arrayBuffer = new ArrayBuffer(false)

  val vertices = Property[List[Double]](Nil)
  val texture = Property[Texture](null)
  val textureCoordinates = Property[List[Double]](Nil)

  private var verticesLength = 0

  onUpdate(vertices, textureCoordinates) {
    arrayBuffer.data = vertices() ::: textureCoordinates()
    verticesLength = vertices().length
  }

  protected def draw() = {
    val texture = this.texture()
    val textureCoordinates = this.textureCoordinates()

    if (texture != null) {
      texture.bind()
    }
    arrayBuffer.bind()
    if (!textureCoordinates.isEmpty) {
      arrayBuffer.bindTextureCoordinates(verticesLength)
    }
    arrayBuffer.drawVertices(0, verticesLength / 3)
  }
}
package org.sgine.ui

import render.ArrayBuffer
import org.sgine.property.Property
import com.badlogic.gdx.graphics.Texture

/**
 * ShapeComponent is a base class for Components that need to render vertices and textures to the
 * screen.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class ShapeComponent extends RenderableComponent {
  private val arrayBuffer = new ArrayBuffer(false)

  protected[ui] val _vertices = Property[List[Double]](Nil)
  protected[ui] val _texture = Property[Texture](null)
  protected[ui] val _textureCoordinates = Property[List[Double]](Nil)

  private var verticesLength = 0

  onUpdate(_vertices, _textureCoordinates) {
    arrayBuffer.data = _vertices() ::: _textureCoordinates()
    verticesLength = _vertices().length
  }

  protected def draw() = {
    val texture = _texture()
    val textureCoordinates = _textureCoordinates()

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
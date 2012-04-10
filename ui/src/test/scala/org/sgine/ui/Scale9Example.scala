package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.Resource
import render.{Vertex, TextureCoordinates}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Scale9Example extends UI {
  val texture = new Texture(Resource("scale9test.png"))

  val shape = new ShapeComponent()
  shape._texture := texture
  shape._vertices := Vertex.scale9(50.0, 50.0, 450.0, 450.0, 0.0, 500.0, 500.0, 200.0, 500.0)
  shape._textureCoordinates := TextureCoordinates.scale9(50.0, 50.0, 450.0, 450.0, 500.0, 500.0)
  contents += shape
}

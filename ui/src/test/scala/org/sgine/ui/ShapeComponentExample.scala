package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.Resource
import render.{Vertex, TextureCoordinates}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object ShapeComponentExample extends UI {
  val texture = new Texture(Resource("sgine.png"))

  val shape = new ShapeComponent()
  shape.texture := texture
  shape.textureCoordinates :=
      TextureCoordinates.rectCoords(0.0, 0.0, 400.0, 96.0, texture.getWidth, texture.getHeight)
  shape.vertices := Vertex.rect(400.0, 96.0)
  contents += shape
}
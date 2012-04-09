package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import render.{Vertex, TextureCoordinates}
import org.sgine.{Updatable, Resource}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object CubeExample extends UI {
  perspective()

  val texture = new Texture(Resource("sgine_256.png"))

  val cube = new ShapeComponent()
  cube._texture := texture
  cube._textureCoordinates := TextureCoordinates.box()
  cube._vertices := Vertex.box(256.0, 256.0, 256.0)
  cube.scale.set(0.001)
  contents += cube

  updates += new Updatable {
    override def update(delta: Double) = {

    }
  }
}
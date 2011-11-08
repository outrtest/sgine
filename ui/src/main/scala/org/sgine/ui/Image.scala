package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.property._
import org.sgine._
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image extends RenderableComponent {
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

  def load(resource: Resource) = {
    texture := new Texture(resource)
    textureRegion.width := 800
    textureRegion.height := 480
    size.width := 800
    size.height := 480
  }

  protected def draw(batch: SpriteBatch) = {
    batch.draw(texture,
      location.x().toFloat,
      location.y().toFloat,
      size.width().toFloat,
      size.height().toFloat,
      textureRegion.x(),
      textureRegion.y(),
      textureRegion.width(),
      textureRegion.height(),
      false,
      false
    )
  }
}
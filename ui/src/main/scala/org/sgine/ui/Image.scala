package org.sgine.ui

import com.badlogic.gdx.graphics.Texture
import org.sgine.property._
import org.sgine._

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Image extends Component {
  val texture = Property[Texture]()

  object location extends PropertyParent {
    val x = Property[Double]()
    val y = Property[Double]()
    val width = Property[Double]()
    val height = Property[Double]()
  }

  object textureRegion extends PropertyParent {
    val x = Property[Int]()
    val y = Property[Int]()
    val width = Property[Int]()
    val height = Property[Int]()
  }

  texture.onChange {
    location.width := texture.getWidth
    location.height := texture.getHeight
    textureRegion.x := 0
    textureRegion.y := 0
    textureRegion.width := texture.getWidth
    textureRegion.height := texture.getHeight
  }

  def load(resource: Resource) = {
    texture := new Texture(resource)
    textureRegion.width := 800
    textureRegion.height := 480
    location.width := 800
    location.height := 480
  }

  def render() = {
    Component.batch.get().draw(texture,
      location.x.toFloat,
      location.y.toFloat,
      location.width.toFloat,
      location.height.toFloat,
      textureRegion.x,
      textureRegion.y,
      textureRegion.width,
      textureRegion.height,
      false,
      false
    )
  }
}
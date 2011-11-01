package org.sgine.ui

import org.sgine.property.{PropertyParent, Property}
import java.lang.ThreadLocal
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent {
  val visible = Property[Boolean](true)

  object location extends PropertyParent {
    val x = Property[Double]()
    val y = Property[Double]()
  }

  object size extends PropertyParent {
    val width = Property[Double]()
    val height = Property[Double]()
  }

}

object Component {
  val batch = new ThreadLocal[SpriteBatch]
}
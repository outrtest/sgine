package org.sgine.ui

import org.sgine.property.{PropertyParent, Property}
import java.lang.ThreadLocal
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.sgine.input.event.MouseEventSupport

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent with MouseEventSupport {
  val visible = Property[Boolean](true)
  val mouseEnabled = Property[Boolean](true)

  object location extends PropertyParent {
    val x = Property[Double](0.0)
    val y = Property[Double](0.0)
  }

  object size extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
  }

  def hitTest(x: Double, y: Double) = {
    if (visible() && mouseEnabled()) {
      val x1 = location.x()
      val x2 = x1 + size.width()
      val y1 = location.y()
      val y2 = y1 + size.height()
      x >= x1 && x <= x2 && y >= y1 && y <= y2
    }
    else {
      false
    }
  }

  def destroy() = {
  }
}

object Component {
  val batch = new ThreadLocal[SpriteBatch]
  val mouse = Property[Component]()
}
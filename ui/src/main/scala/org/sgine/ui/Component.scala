package org.sgine.ui

import org.sgine.input.event.MouseEventSupport
import org.sgine.property.{PropertyParent, Property}
import org.sgine.{Listenable, UpdatableInvocation}
import com.badlogic.gdx.math.collision.{BoundingBox, Ray}
import com.badlogic.gdx.math.{Matrix4, Vector3, Intersector}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent with MouseEventSupport with UpdatableInvocation {
  protected val matrix = new Matrix4()

  onUpdate(location.x, location.y, location.z) {
    matrix.idt() // TODO: use parent matrix instead
    matrix.translate(location.x().toFloat, location.y().toFloat, location.z().toFloat)
  }

  val visible = Property[Boolean](true)
  val mouseEnabled = Property[Boolean](true)

  object location extends PropertyParent {
    val x = Property[Double](0.0)
    val y = Property[Double](0.0)
    val z = Property[Double](0.0)
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

  def hitTest(ray: Ray) = {
    Component.tempVector1.set((location.x() - (size.width() / 2.0)).toFloat, (location.y() - (size.height() / 2.0)).toFloat, location.z().toFloat)
    Component.tempVector2.set((location.x() + (size.width() / 2.0)).toFloat, (location.y() + (size.height() / 2.0)).toFloat, location.z().toFloat)
    Component.tempBoundingBox.set(Component.tempVector1, Component.tempVector2)
    //    Component.tempBoundingBox.mul(matrix)     // Need to multiply the parent matrix, not the component matrix
    Intersector.intersectRayBoundsFast(ray, Component.tempBoundingBox)
  }

  def onUpdate(listenables: Listenable[_]*)(f: => Unit) = {
    val function = () => f
    listenables.foreach(l => {
      val listener = (oldValue: Any, newValue: Any) => {
        delayedHandling(l, function)
      }
      l.listeners += listener
    })
  }

  def destroy() = {
  }
}

object Component {
  private val tempBoundingBox = new BoundingBox()
  private val tempVector1 = new Vector3()
  private val tempVector2 = new Vector3()

  val mouse = Property[Component]()
}
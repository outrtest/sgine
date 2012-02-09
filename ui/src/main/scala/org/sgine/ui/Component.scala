package org.sgine.ui

import org.sgine.property.{PropertyParent, Property}
import com.badlogic.gdx.math.collision.{BoundingBox, Ray}
import com.badlogic.gdx.math.{Matrix4, Vector3, Intersector}
import org.sgine.scene.Element
import org.sgine.event.{ChangeEvent, Listenable}
import org.sgine.{Updater, Updatable, AsynchronousInvocation}

/**
 * Component is the base class for all visual elements in UI.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent with Listenable with Element with Updater {
  /**
   * The parent associated with this Component.
   */
  override val parent: Property[Component] = Property[Component]()

  /**
   * World matrix for this component.
   */
  protected[ui] val matrix = new Matrix4()

  private val updateAsync = new AsynchronousInvocation()

  onUpdate(location.x, location.y, location.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z) {
    matrix.idt() // TODO: use parent matrix instead
    matrix.translate(location.x().toFloat, location.y().toFloat, location.z().toFloat)
    matrix.rotate(rotation.x().toFloat, rotation.y().toFloat, rotation.z().toFloat, 1.0f)
    matrix.scale(scale.x().toFloat, scale.y().toFloat, scale.z().toFloat)

//    println("T: %sx%sx%s, R: %sx%sx%s, S: %sx%sx%s".format(location.x(), location.y(), location.z(), rotation.x(), rotation.y(), rotation.z(), scale.x(), scale.y(), scale.z()))
  }

  /**
   * The visibility of this component.
   */
  val visible = Property[Boolean](true)
  /**
   * True if mouse events should occur on this Component.
   */
  val mouseEnabled = Property[Boolean](true)

  /**
   * The local location of this component in the UI.
   */
  object location extends Property3D(0.0, 0.0, 0.0)

  /**
   * The size of this component in the UI.
   */
  object size extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
  }

  /**
   * The scale of this component in the UI.
   */
  object scale extends Property3D(1.0, 1.0, 1.0)

  /**
   * The rotation of this component in the UI.
   */
  object rotation extends Property3D(0.0, 0.0, 0.0)

  object updates {
    def +=(updatable: Updatable) = add(updatable)
    def -=(updatable: Updatable) = remove(updatable)
  }

  /**
   * Tests whether the supplied Ray intersects with this Component.
   */
  def hitTest(ray: Ray) = {
    val w = (size.width() / 2.0).toFloat
    val h = (size.height() / 2.0).toFloat
    Component.tempVector1.set(-w, -h, 0.0f)
    Component.tempVector2.set(w, h, 0.0f)
    Component.tempBoundingBox.set(Component.tempVector1, Component.tempVector2)
    Component.tempBoundingBox.mul(matrix)
    Intersector.intersectRayBoundsFast(ray, Component.tempBoundingBox)
  }

  override def update(delta: Double) = {
    super.update(delta)

    updateAsync.invokeNow()
  }

  def onUpdate(listenables: Listenable*)(f: => Unit) = {
    val function = () => f
    listenables.foreach(l => l.listeners.synchronous.filtered[ChangeEvent[_]] {
      case event => updateAsync.invokeLater(function)
    })
  }

  /**
   * Adds change listeners to the Listenables to invoke the supplied function immediately when a
   * change occurs.
   */
  def onChange(listenables: Listenable*)(f: => Unit) = listenables.foreach(l => l.listeners.synchronous.filtered[ChangeEvent[_]] {
    case event => f
  })

  /**
   * Called upon destruction of this Component.
   */
  def destroy() = {
  }
}

class Property3D(dx: Double, dy: Double, dz: Double) extends PropertyParent {
  val x = Property[Double](dx)
  val y = Property[Double](dy)
  val z = Property[Double](dz)

  /**
   * Assigns the default value to these properties.
   */
  def default() = {
    apply(dx, dy, dz)
  }

  /**
   * Modifies the encapsulated values. Defaults to the current value if not specified.
   */
  def apply(x: Double = this.x(), y: Double = this.y(), z: Double = this.z()) = {
    this.x := x
    this.y := y
    this.z := z
  }

  /**
   * Sets x, y, and z to the value supplied.
   */
  def set(value: Double) = apply(value, value, value)
}

object Component {
  private val tempBoundingBox = new BoundingBox()
  private val tempVector1 = new Vector3()
  private val tempVector2 = new Vector3()

  /**
   * The Component the mouse is current over.
   */
  val mouse = Property[Component]()
}
package org.sgine.ui

import align.{DepthAlignment, HorizontalAlignment, VerticalAlignment}
import org.sgine.property.{PropertyParent, Property}
import com.badlogic.gdx.math.collision.{BoundingBox, Ray}
import com.badlogic.gdx.math.{Matrix4, Vector3, Intersector}
import org.sgine.event.{ChangeEvent, Listenable}
import org.sgine.{Updater, Updatable, AsynchronousInvocation}

import scala.math._
import org.sgine.hierarchy.Element

/**
 * Component is the base class for all visual elements in UI.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent with Listenable with Element with Updater {
  /**
   * The parent associated with this Component.
   */
  override def parent = super.parent.asInstanceOf[Container]

  /**
   * Return the root UI for this Component or null if not attached to a UI.
   */
  def ui: UI = this match {
    case instance: UI => instance
    case _ => parent match {
      case null => null
      case p => p.ui
    }
  }

  /**
   * World matrix for this component.
   */
  protected[ui] val matrix = new Matrix4()
  protected val multipliedAlpha = Property[Float](1.0f)

  private val updateAsync = new AsynchronousInvocation()

  /**
   * The visibility of this component.
   *
   * Defaults to true.
   */
  val visible = Property[Boolean](true)
  /**
   * The transparency value assigned to the component.
   *
   * Defaults to 1.0 (opaque)
   */
  val alpha = Property[Double](1.0)
  /**
   * True if mouse events should occur on this Component.
   *
   * Defaults to true.
   */
  val mouseEnabled = Property[Boolean](true)
  /**
   * If true the translation will be independent from the parent matrix.
   *
   * Defaults to false.
   */
  val localizeMatrix = Property[Boolean](false)
  /**
   * Updater function called when a translation value of the component is modified to apply properly to the backing
   * Matrix4.
   *
   * Defaults to Component.DefaultMatrixUpdater
   */
  val matrixUpdater = Property[(Component, Matrix4) => Unit](Component.DefaultMatrixUpdater)

  /**
   * The local location of this component in the UI.
   */
  object location extends Property3D(0.0, 0.0, 0.0) {
    val actual = new Property3D(0.0, 0.0, 0.0)

    def updateActual() = {
      alignment.horizontal() match {
        case HorizontalAlignment.Center => actual.x := x()
        case HorizontalAlignment.Left => actual.x := x() + (size.width() / 2.0)
        case HorizontalAlignment.Right => actual.x := x() - (size.width() / 2.0)
      }
      alignment.vertical() match {
        case VerticalAlignment.Middle => actual.y := y()
        case VerticalAlignment.Top => actual.y := y() - (size.height() / 2.0)
        case VerticalAlignment.Bottom => actual.y := y() + (size.height() / 2.0)
      }
      alignment.depth() match {
        case DepthAlignment.Middle => actual.z := z()
        case DepthAlignment.Front => actual.z := z() + (size.depth() / 2.0)
        case DepthAlignment.Back => actual.z := z() - (size.depth() / 2.0)
      }
    }
  }

  /**
   * The alignment of this component
   */
  object alignment extends PropertyParent {
    val horizontal = Property[HorizontalAlignment](HorizontalAlignment.Center)
    val vertical = Property[VerticalAlignment](VerticalAlignment.Middle)
    val depth = Property[DepthAlignment](DepthAlignment.Middle)
  }

  /**
   * The size of this component in the UI.
   */
  object size extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
    val depth = Property[Double](0.0)
    /**
     * The algorithm used to update the actual size when the measured size changes.
     *
     * Defaults to SizeAlgorithm.measured
     */
    val algorithm = Property[SizeAlgorithm](SizeAlgorithm.measured)
  }

  /**
   * The measured size of the text.
   */
  object measured extends PropertyParent {
    val width = Property[Double](0.0)
    val height = Property[Double](0.0)
    val depth = Property[Double](0.0)
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

  def resolution(width: Double, height: Double) = {
    val w = 0.8275 / width
    val h = 0.8275 / height
    localizeMatrix := true
    scale.set(min(w, h))
  }

  onUpdate(location.actual.x, location.actual.y, location.actual.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z, localizeMatrix) {
    updateMatrix()
  }

  protected[ui] def updateMatrix() = {
    matrixUpdater() match {
      case null => // Do nothing
      case updater => updater(this, matrix)
    }
  }

  protected[ui] def updateAlpha(): Unit = {
    val parentAlpha = parent match {
      case null => 1.0
      case p => p.alpha()
    }
    multipliedAlpha := (alpha() * parentAlpha).toFloat
  }

  // Update size when measured size is modified in the case of autoSize being true.
  onChange(measured.width, measured.height, measured.depth) {
    size.algorithm() match {
      case null => // Do nothing
      case algorithm => algorithm(this)
    }
  }

  onChange(location.x,
    location.y,
    location.z,
    alignment.horizontal,
    alignment.vertical,
    alignment.depth,
    size.width,
    size.height,
    size.depth) {
    location.updateActual()
  }

  onChange(alpha) {
    updateAlpha()
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
    listenables.foreach(l => l.listeners.synchronous {
      case event: ChangeEvent[_] => updateAsync.invokeLater(function)
    })
  }

  /**
   * Adds change listeners to the Listenables to invoke the supplied function immediately when a
   * change occurs.
   */
  def onChange(listenables: Listenable*)(f: => Unit) = listenables.foreach(l => l.listeners.synchronous {
    case event: ChangeEvent[_] => f
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

  /**
   * Default MatrixUpdater used by Components to apply translation, rotation, and scale.
   */
  val DefaultMatrixUpdater: (Component, Matrix4) => Unit = (component: Component, matrix: Matrix4) => {
    component.parent match {
      case null => matrix.idt()
      case p => component.localizeMatrix() match {
        case true => matrix.idt()
        case false => matrix.set(p.matrix)
      }
    }
    matrix.translate(component.location.actual.x().toFloat, component.location.actual.y().toFloat, component.location.actual.z().toFloat)
    val maxRotation = max(max(component.rotation.x(), component.rotation.y()), component.rotation.z())
    matrix.rotate((component.rotation.x() / maxRotation).toFloat, (component.rotation.y() / maxRotation).toFloat, (component.rotation.z() / maxRotation).toFloat, maxRotation.toFloat)
    matrix.scale(component.scale.x().toFloat, component.scale.y().toFloat, component.scale.z().toFloat)
  }
}
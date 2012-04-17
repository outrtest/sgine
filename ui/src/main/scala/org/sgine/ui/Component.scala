package org.sgine.ui

import align.{DepthAlignment, HorizontalAlignment, VerticalAlignment}
import com.badlogic.gdx.math.collision.{BoundingBox, Ray}
import com.badlogic.gdx.math.{Matrix4, Vector3, Intersector}
import org.sgine.event.{ChangeEvent, Listenable}

import scala.math._
import org.sgine.hierarchy.Element
import org.sgine.property.{NumericProperty, PropertyParent, Property}
import org.sgine._
import concurrent.AtomicInt
import naming.NamedChild

/**
 * Component is the base class for all visual elements in UI.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
trait Component extends PropertyParent with Listenable with Element with Updater {
  /**
   * The parent associated with this Component.
   */
  override def parent = super.parent.asInstanceOf[AbstractContainer]

  /**
   * Unique identifier in this runtime for this specific component instance.
   */
  val id = Component.nextId()

  private var _name: String = null

  def name: String = _name match {
    case null => "%s%s".format(getClass.getSimpleName, id)
    case n => n
  }

  def name_=(name: String) = _name = name

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

  private val updateAsync = new AsynchronousInvocation()

  /**
   * The visibility of this component.
   *
   * Defaults to true.
   */
  val visible = Property[Boolean]("visible", true)

  /**
   * True if mouse events should occur on this Component.
   *
   * Defaults to true.
   */
  val mouseEnabled = Property[Boolean]("mouseEnabled", true)
  /**
   * If true the translation will be independent from the parent matrix.
   *
   * Defaults to false.
   */
  val localizeMatrix = Property[Boolean]("localizeMatrix", false)
  /**
   * Updater function called when a translation value of the component is modified to apply properly to the backing
   * Matrix4.
   *
   * Defaults to Component.DefaultMatrixUpdater
   */
  val matrixUpdater = Property[(Component, Matrix4) => Unit]("matrixUpdater", Component.DefaultMatrixUpdater)

  /**
   * The local location of this component in the UI.
   */
  object location extends Property3D(this, 0.0, 0.0, 0.0) {
    val actual = new Property3D(this, 0.0, 0.0, 0.0)

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
  object alignment extends ComponentPropertyParent(this) {
    val horizontal = Property[HorizontalAlignment]("horizontal", HorizontalAlignment.Center)
    val vertical = Property[VerticalAlignment]("vertical", VerticalAlignment.Middle)
    val depth = Property[DepthAlignment]("depth", DepthAlignment.Middle)
  }

  /**
   * The size of this component in the UI.
   */
  object size extends ComponentPropertyParent(this) {
    val width = NumericProperty("width", 0.0)
    val height = NumericProperty("height", 0.0)
    val depth = NumericProperty("depth", 0.0)
    /**
     * The algorithm used to update the actual size when the measured size changes.
     *
     * Defaults to SizeAlgorithm.measured
     */
    val algorithm = Property[SizeAlgorithm]("algorithm", SizeAlgorithm.measured)

    def apply(width: Double = this.width(), height: Double = this.height(), depth: Double = this.depth()) = {
      this.width := width
      this.height := height
      this.depth := depth
    }
  }

  /**
   * The measured size of the text.
   */
  object measured extends ComponentPropertyParent(this) {
    val width = NumericProperty("width", 0.0)
    val height = NumericProperty("height", 0.0)
    val depth = NumericProperty("depth", 0.0)
  }

  /**
   * The scale of this component in the UI.
   */
  object scale extends Property3D(this, 1.0, 1.0, 1.0)

  /**
   * The rotation of this component in the UI.
   */
  object rotation extends Property3D(this, 0.0, 0.0, 0.0)

  /**
   * The color of this component.
   */
  object color extends ComponentPropertyParent(this) {
    val red = NumericProperty("red", 1.0)
    val green = NumericProperty("green", 1.0)
    val blue = NumericProperty("blue", 1.0)
    /**
     * The transparency value assigned to the component.
     *
     * Defaults to 1.0 (opaque)
     */
    val alpha = NumericProperty("alpha", 1.0)

    def apply(color: Color, updateAlpha: Boolean = true) = {
      red := color.red
      green := color.green
      blue := color.blue
      if (updateAlpha) {
        alpha := color.alpha
      }
    }

    def update(color: MutableColor) = {
      color.red = red()
      color.green = green()
      color.blue = blue()
      color.alpha = alpha()
    }

    object actual extends ComponentPropertyParent(this) {
      val red = NumericProperty("red", 1.0)
      val green = NumericProperty("green", 1.0)
      val blue = NumericProperty("blue", 1.0)
      val alpha = NumericProperty("alpha", 1.0)
    }
  }

  object updates {
    def +=(updatable: Updatable) = add(updatable)

    def -=(updatable: Updatable) = remove(updatable)

    /**
     * Invokes supplied function with the elapsed time when <code>time</code> has elapsed.
     */
    def every(time: Double)(f: Double => Unit) = {
      add(new Updatable {
        private var elapsed = 0.0

        override def update(delta: Double) = {
          elapsed += delta
          if (elapsed >= time) {
            f(elapsed)
            elapsed = 0.0
          }
        }
      })
    }
  }

  def resolution(width: Double, height: Double, maintainAspectRatio: Boolean = true) = {
    val w = 0.8275 / width
    val h = 0.8275 / height
    localizeMatrix := true
    if (maintainAspectRatio) {
      scale.set(min(w, h))
    } else {
      scale(w, h, 1.0)
    }
  }

  onUpdate(location.actual.x, location.actual.y, location.actual.z, rotation.x, rotation.y, rotation.z, scale.x, scale.y, scale.z, localizeMatrix) {
    validateMatrix()
  }

  def invalidateMatrix() = updateAsync.invokeLater(validateMatrix)

  private val validateMatrix = () => updateMatrix()

  protected[ui] def updateMatrix() = {
    matrixUpdater() match {
      case null => // Do nothing
      case updater => updater(this, matrix)
    }
  }

  protected[ui] def updateColor(): Unit = {
    parent match {
      case null => Component.tempColor(Color.White)
      case p => p.color.update(Component.tempColor)
    }
    color.actual.red := color.red() * Component.tempColor.red
    color.actual.green := color.green() * Component.tempColor.green
    color.actual.blue := color.blue() * Component.tempColor.blue
    color.actual.alpha := color.alpha() * Component.tempColor.alpha
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

  onChange(color.red, color.green, color.blue, color.alpha) {
    updateColor()
  }

  /**
   * Tests whether the supplied Ray intersects with this Component.
   */
  def hitTest(ray: Ray, intersection: Vector3) = {
    val w = (size.width() / 2.0).toFloat
    val h = (size.height() / 2.0).toFloat
    Component.tempVector1.set(-w, -h, 0.0f).mul(matrix)
    Component.tempVector2.set(w, h, 0.0f).mul(matrix)
    Component.tempBoundingBox.set(Component.tempVector1, Component.tempVector2)
//    Component.tempBoundingBox.mul(matrix)
//    Component.tempVector1.set(0.0f, 0.0f, 0.0f)
//    Component.tempVector1.mul(matrix)
    Intersector.intersectRayBoundsFast(ray, Component.tempBoundingBox) match {
      case true => {
        if (intersection != null) {
//          println("Intersected: %s".format(Component.tempVector1))
          val radius = Component.tempBoundingBox.getDimensions.len() / 2.0f
          Intersector.intersectRaySphere(ray, Component.tempBoundingBox.getCenter, radius, intersection)
        }
        true
      }
      case false => false
    }
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

class ComponentPropertyParent(val parent: PropertyParent) extends PropertyParent {
  val name = getClass.getSimpleName.substring(getClass.getSimpleName.indexOf('$') + 1).replaceAll("\\$", "")
}

class Property3D(parent: PropertyParent, dx: Double, dy: Double, dz: Double) extends ComponentPropertyParent(parent) {
  val x = NumericProperty("x", dx)
  val y = NumericProperty("y", dy)
  val z = NumericProperty("z", dz)

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

object Component extends PropertyParent with NamedChild {
  val parent: PropertyParent = null

  private val tempBoundingBox = new BoundingBox()
  private val tempVector1 = new Vector3()
  private val tempVector2 = new Vector3()
  private val tempColor = new MutableColor(1.0, 1.0, 1.0, 1.0)

  private val identifiers = new AtomicInt(0)

  def nextId() = identifiers.addAndGet(1)

  /**
   * The Component the mouse is current over.
   */
  val mouse = Property[Component]("mouse")

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
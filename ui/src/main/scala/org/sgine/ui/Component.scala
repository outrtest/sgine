package org.sgine.ui

import com.badlogic.gdx.math.collision.{BoundingBox, Ray}
import com.badlogic.gdx.math.{Matrix4, Vector3, Intersector}
import internal._
import org.sgine.event.{ChangeEvent, Listenable}

import scala.math._
import org.sgine._
import concurrent.AtomicInt
import hierarchy.{Child, Element}
import naming.NamedChild
import annotation.tailrec
import property.{PropertyParent, Property}
import theme.Theme

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

  protected val updateAsync = new AsynchronousInvocation()

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
   * Determines whether containers that are laying out components should include this component in the layout.
   *
   * Defaults to true.
   */
  val includeInLayout = Property[Boolean]("includeInLayout", true)
  /**
   * Determines whether this Component can be themed.
   *
   * Defaults to true.
   */
  val themable = Property[Boolean]("themable", true)

  /**
   * The local location of this component in the UI.
   */
  val location = new ComponentLocation(this)

  /**
   * The alignment of this component
   */
  val alignment = new ComponentAlignment(this)

  /**
   * Padding to this component
   */
  val padding = new ComponentPadding(this)

  /**
   * The size of this component in the UI.
   */
  val size = new ComponentSize(this)

  /**
   * The scale of this component in the UI.
   */
  val scale = new Property3D("scale", this, 1.0, 1.0, 1.0)

  /**
   * The rotation of this component in the UI.
   */
  val rotation = new Property3D("rotation", this, 0.0, 0.0, 0.0)

  /**
   * The color of this component.
   */
  val color = new ComponentColor(this)

  val updates = new ComponentUpdates(this)

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

  onUpdate(location.actual.x,
           location.actual.y,
           location.actual.z,
           rotation.x,
           rotation.y,
           rotation.z,
           scale.x,
           scale.y,
           scale.z,
           padding.top,
           padding.bottom,
           padding.left,
           padding.right,
           localizeMatrix) {
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
  onChange(size.measured.width, size.measured.height, size.measured.depth) {
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
    if (mouseEnabled()) {
      val w = (size.width() / 2.0).toFloat
      val h = (size.height() / 2.0).toFloat
      Component.tempVector1.set(-w, -h, 0.0f).mul(matrix)
      Component.tempVector2.set(w, h, 0.0f).mul(matrix)
      Component.tempBoundingBox.set(Component.tempVector1, Component.tempVector2)
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
    } else {
      false
    }
  }

  override protected def initialize() = {
    super.initialize()

    if (themable()) {
      Theme(this)       // Apply theme to this component
    }
  }

  override def update(delta: Double) = {
    super.update(delta)

    updateAsync.invokeNow()
  }

  def onUpdate(listenables: Listenable*)(f: => Unit) = {
    val function = () => f
    listenables.foreach(l => l.listeners.synchronous {
      case event: ChangeEvent => updateAsync.invokeLater(function)
    })
  }

  /**
   * Called upon destruction of this Component.
   */
  def destroy() = {
  }
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
  val mouse = Property[Component]("mouse", null)

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
    var padTop = 0.0
    var padBottom = 0.0
    var padLeft = 0.0
    var padRight = 0.0
    if (component.includeInLayout() && component.parent != null) {
      padLeft = component.parent.padding.left()
      padRight = component.parent.padding.right()
      padTop = component.parent.padding.top()
      padBottom = component.parent.padding.bottom()
    }
    val x = component.location.actual.x() + padLeft - padRight
    val y = component.location.actual.y() + padBottom - padTop
    val z = component.location.actual.z()
    matrix.translate(x.toFloat, y.toFloat, z.toFloat)
    val rx = correctRotation(component.rotation.x())
    val ry = correctRotation(component.rotation.y())
    val rz = correctRotation(component.rotation.z())
    val maxRotation = max(max(rx, ry), rz)
    matrix.rotate((rx / maxRotation).toFloat, (ry / maxRotation).toFloat, (rz / maxRotation).toFloat, maxRotation.toFloat)
    matrix.scale(component.scale.x().toFloat, component.scale.y().toFloat, component.scale.z().toFloat)
  }

  @tailrec
  private def correctRotation(value: Double): Double = {
    if (value >= 0.0) {
      value
    } else {
      correctRotation(value + 360.0)
    }
  }

  @tailrec
  def parentComponent(child: Child): Component = {
    child match {
      case null => null
      case component: Component => component
      case c => c.parent match {
        case p: Child => parentComponent(p)
        case _ => null
      }
    }
  }
}
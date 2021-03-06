package org.sgine.ui

import com.badlogic.gdx.{InputProcessor, Gdx, ApplicationListener}
import layout.LayoutableContainer
import org.powerscala.hierarchy.ContainerView
import org.sgine.input.{Mouse, MouseButton, Key, Keyboard}
import org.sgine.input.event._

import scala.math._
import org.powerscala.Updatable
import com.badlogic.gdx.graphics._
import com.badlogic.gdx.math.collision.Ray
import com.badlogic.gdx.math.Vector3
import org.powerscala.concurrent.Time
import org.powerscala.event.{Listenable, ChangeEvent}
import org.powerscala.property.{ObjectPropertyParent, MutableProperty, Property}

/**
 * UI provides a base class to be extended and allow an initialization end-point for the graphical application to start.
 *
 * The graphical context will be loaded at the time the body is evaluated, so all initialization can be done within.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class UI extends Container with LayoutableContainer with DelayedInit with FocusManager {
  /**
   * ContainerView of all Components within this UI hierarchy.
   */
  lazy val componentsView = new ContainerView[Component](this)
  /**
   * ContainerView of all RenderableComponents within this UI hierarchy.
   */
  lazy val rendererView = new ContainerView[RenderableComponent](this)
  /**
   * ContainerView of all Updatables within this UI hierarchy.
   */
  lazy val updatablesView = new ContainerView[Updatable](this)

  /**
   * The camera to be used for displaying this UI.
   *
   * Defaults to an OrthographicCamera with the width and height defined by the UI.
   */
  lazy val camera = Property[Camera]("camera", createOrtho(width(), height()))

  /**
   * Whether vertical synchronization should be enabled.
   *
   * Defaults to false.
   */
  lazy val verticalSync = Property[Boolean]("verticalSync", false)

  /**
   * Defines a fixed timestep to use instead of delta from previous render.
   *
   * Defaults to 0.0 (disabled)
   */
  lazy val fixedTimestep = Property[Double]("fixedTimestep", 0.0)

  private var delayedInitialize: () => Unit = _

  /**
   * Window title
   */
  def title = getClass.getSimpleName.replaceAll("\\$", "")

  /**
   * Window width
   *
   * Defaults to 1024
   */
  lazy val width = Property[Int]("width", 1024)

  /**
   * Window height
   *
   * Defaults to 768
   */
  lazy val height = Property[Int]("height", 768)

  /**
   * Fullscreen mode
   *
   * Defaults to false
   */
  lazy val fullscreen = Property[Boolean]("fullscreen", false)

  /**
   * The currently active FocusManager.
   *
   * Defaults to the UI.
   */
  lazy val focusManager = Property[FocusManager]("focusManager", this)

  /**
   * True if this UI instance is the currently active one.
   */
  lazy val active: Property[Boolean] with Listenable = Property[Boolean]("active", false)

  def activate() = UI.current := this

  /**
   * Bits for red channel.
   *
   * Defaults to 8
   */
  def red = 8

  /**
   * Bits for green channel.
   *
   * Defaults to 8
   */
  def green = 8

  /**
   * Bits for blue channel.
   *
   * Defaults to 8
   */
  def blue = 8

  /**
   * Bits for alpha channel.
   *
   * Defaults to 8
   */
  def alpha = 8

  /**
   * Depth bits.
   *
   * Defaults to 16
   */
  def depth = 16

  /**
   * Stencil buffer.
   *
   * Defaults to 8
   */
  def stencil = 8

  /**
   * Samples for MSAA
   *
   * Defaults to 16
   */
  def samples = 16

  /**
   * The delta in seconds for the current render.
   */
  def delta = _delta

  private var _delta: Double = 0.0
  private var currentRay: Ray = _
  private lazy val intersection = new Vector3

  lazy val pickFunction = (current: Component, c: Component) => if (c.hitTest(currentRay, intersection)) {
    c
  } else {
    current
  }

  private def pickComponents(evt: MouseEvent, components: Iterable[Component]): Unit = {
    val x = evt.x
    val y = evt.y
    currentRay = camera().getPickRay(x.toFloat, y.toFloat)
    val hit = components.foldLeft(null.asInstanceOf[Component])(pickFunction)
    val localEvent: MouseEvent = hit match {
      case null => null
      case _ => evt.duplicate(intersection.x, intersection.y, intersection.z)
    }
    if (hit != null) {
      hit.fire(localEvent)
    }
    if (evt.isInstanceOf[MouseMoveEvent] && Component.mouse() != hit) {
      val old = Component.mouse()
      Component.mouse := hit
      if (old != null) {
        old.fire(MouseOutEvent(intersection.x, intersection.y, intersection.z, evt.deltaX, evt.deltaY))
      }
      if (hit != null) {
        hit.fire(MouseOverEvent(intersection.x, intersection.y, intersection.z, evt.deltaX, evt.deltaY))
      }
    }
  }

  def delayedInit(x: => Unit) = {
    size.width := width()
    size.height := height()

    delayedInitialize = () => x

  }

  /**
   * Convenience method to replace the camera with an OrthographicCamera.
   */
  final def orthographic(width: Double = this.width(), height: Double = this.height()) = {
    camera := createOrtho(width, height)
  }

  private def createOrtho(width: Double, height: Double) = {
    val c = new OrthographicCamera(width.toFloat, height.toFloat)
    c.near = 0.1f
    c.far = 10000.0f
    c.translate(0.0f, 0.0f, 1000.0f)
    c.update()
    c
  }

  /**
   * Convenience method to replace the camera with a PerspectiveCamera.
   */
  final def perspective(fov: Double = 45.0, nearPlane: Double = 0.1, farPlane: Double = 1000.0) = {
    val aspectRatio = width() / height()
    val c = new PerspectiveCamera(fov.toFloat, 2.0f * aspectRatio, 2.0f)
    c.near = nearPlane.toFloat
    c.far = farPlane.toFloat
    c.translate(0.0f, 0.0f, 1.0f)
    c.update()
    camera := c
  }

  private lazy val applicationInstance = createInstance()

  final def main(args: Array[String]): Unit = {
    val graphics = applicationInstance.getGraphics
    onUpdate(width, height, fullscreen) {
      graphics.setDisplayMode(width(), height(), fullscreen())
    }
  }

  def exit() = applicationInstance.exit()

  private def createInstance() = {
    Texture.setEnforcePotImages(false) // No need to enforce power-of-two images

    // Work-around so we don't need LWJGL functionality in separate project

    val configClass = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration")
    val config = configClass.newInstance().asInstanceOf[AnyRef]
    configClass.getField("r").set(config, red)
    configClass.getField("g").set(config, green)
    configClass.getField("b").set(config, blue)
    configClass.getField("a").set(config, alpha)
    configClass.getField("depth").set(config, depth)
    configClass.getField("stencil").set(config, stencil)
    configClass.getField("samples").set(config, samples)
    configClass.getField("width").set(config, width())
    configClass.getField("height").set(config, height())
    configClass.getField("fullscreen").set(config, fullscreen())
    configClass.getField("useGL20").set(config, false)
    configClass.getField("useCPUSynch").set(config, false)
    configClass.getField("resizable").set(config, false)
    configClass.getField("title").set(config, title)

    val clazz = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplication")
    val constructor = clazz.getConstructor(classOf[ApplicationListener], configClass)
    constructor.newInstance(List[AnyRef](applicationListener, config): _*).asInstanceOf[com.badlogic.gdx.Application]
  }

  lazy val applicationListener = new ApplicationListener with InputProcessor {
    def create() = {
      Gdx.gl11.glShadeModel(GL10.GL_SMOOTH)
      Gdx.gl11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
      Gdx.gl11.glClearDepthf(1.0f)
      Gdx.gl11.glEnable(GL10.GL_BLEND)
      Gdx.gl11.glEnable(GL10.GL_DEPTH_TEST)
      Gdx.gl11.glDepthFunc(GL10.GL_LEQUAL)
      Gdx.gl11.glEnable(GL10.GL_TEXTURE_2D)
      Gdx.gl11.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)
      camera().update()
      Gdx.input.setInputProcessor(this)
      if (delayedInitialize != null) {
        delayedInitialize()
      }
      Mouse.listeners.synchronous {
        case evt: MouseEvent => pickComponents(evt, componentsView)
      }
      UI.current := UI.this
      onUpdate(camera) {
        camera().update()
      }
      onUpdate(verticalSync) {
        Gdx.graphics.setVSync(verticalSync())
      }

      Gdx.graphics.setVSync(verticalSync())
    }

    def resize(width: Int, height: Int) = {
    }

    private var previousTime = 0L
    def render() = {
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT) // TODO: optional?
      _delta = fixedTimestep() match {
        case 0.0 => {
          val currentTime = System.nanoTime()
          val d = if (previousTime == 0L) {
            0.0
          } else {
            Time.fromNanos(currentTime - previousTime)
          }
          previousTime = currentTime
          d
        }
        case d => d
      }
      updateKeys()
      update(delta)
      updatablesView.foreach(updateUpdatables)
      rendererView.foreach(renderRenderable)
    }

    private val updateUpdatables = (updatable: Updatable) => updatable.update(delta)

    private val renderRenderable = (renderable: RenderableComponent) => if (renderable.isVisible) renderable.render()

    def pause() = {
    }

    def resume() = {
    }

    def dispose() = {
    }

    private val updateKey = (key: Key) => {
      if (key.pressed) {
        KeyPressedEvent.update(key.event, delta, UI.this)
      }
    }

    private def updateKeys() = {
      Key.values.foreach(updateKey)
    }

    def keyDown(keyCode: Int) = {
      activate()
      val key = Key.byKeyCode(keyCode).getOrElse(throw new RuntimeException("Unknown keyCode %s".format(keyCode)))
      Keyboard.fire(KeyDownEvent(key))
      fire(KeyDownEvent(key))
      KeyPressedEvent.update(key.event, delta, UI.this)
      true
    }

    def keyTyped(c: Char) = {
      activate()
      Keyboard.fire(KeyTypeEvent(c))
      fire(KeyTypeEvent(c))
      true
    }

    def keyUp(keyCode: Int) = {
      activate()
      val key = Key.byKeyCode(keyCode).getOrElse(throw new RuntimeException("Unknown keyCode %s".format(keyCode)))
      Keyboard.fire(KeyUpEvent(key))
      fire(KeyUpEvent(key))
      KeyPressedEvent.released(key.event)
      true
    }

    def scrolled(amount: Int) = {
      activate()
      Mouse.fire(MouseWheelEvent(amount, Mouse.x(), Mouse.x(), 0.0, 0.0, 0.0))
      fire(MouseWheelEvent(amount, Mouse.x(), Mouse.x(), 0.0, 0.0, 0.0))
      true
    }

    def touchDown(x: Int, y: Int, pointer: Int, button: Int) = {
      activate()
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      Mouse.fire(MousePressEvent(MouseButton(button), x, y, 0.0, dx, dy))
      fire(MousePressEvent(MouseButton(button), x, y, 0.0, dx, dy))
      Mouse.x := x
      Mouse.y := y
      true
    }

    def touchDragged(x: Int, y: Int, pointer: Int) = {
      activate()
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      Mouse.x := x
      Mouse.y := y
      Mouse.fire(MouseDragEvent(x, y, 0.0, dx, dy))
      fire(MouseDragEvent(x, y, 0.0, dx, dy))
      true
    }

    def touchMoved(x: Int, y: Int) = {
      activate()
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      // TODO: support deferred mouse events
      Mouse.x := x
      Mouse.y := y
      Mouse.fire(MouseMoveEvent(x, y, 0.0, dx, dy))
      fire(MouseMoveEvent(x, y, 0.0, dx, dy))
      true
    }

    def touchUp(x: Int, y: Int, pointer: Int, button: Int) = {
      activate()
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      Mouse.x := x
      Mouse.y := y
      Mouse.fire(MouseReleaseEvent(MouseButton(button), x, y, 0.0, dx, dy))
      fire(MouseReleaseEvent(MouseButton(button), x, y, 0.0, dx, dy))
      true
    }
  }
}

object UI extends ObjectPropertyParent(null) {
  /**
   * The currently active UI
   */
  val current = Property[UI]("current", null)

  current.listeners.synchronous {
    case evt: ChangeEvent => {
      evt.oldValue match {
        case ui: UI => ui.active.asInstanceOf[MutableProperty[Boolean]] := false
        case _ => // Nothing
      }
      evt.newValue match {
        case ui: UI => ui.active.asInstanceOf[MutableProperty[Boolean]] := true
        case _ => // Nothing
      }
    }
  }
}
package org.sgine.ui

import com.badlogic.gdx.{InputProcessor, Gdx, ApplicationListener}
import org.sgine.scene.ContainerView
import org.sgine.input.{Mouse, MouseButton, Key, Keyboard}
import org.sgine.input.event._

import scala.math._
import org.sgine.Updatable
import org.sgine.property.{Property, ImmutableProperty}
import java.lang.ThreadLocal
import com.badlogic.gdx.graphics._

/**
 * UI provides a base class to be extended and allow an initialization end-point for the graphical application to start.
 *
 * The graphical context will be loaded at the time the body is evaluated, so all initialization can be done within.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class UI extends Container with DelayedInit {
  lazy val componentsView = new ImmutableProperty(new ContainerView[Component](this))
  lazy val rendererView = new ImmutableProperty(new ContainerView[RenderableComponent](this))
  lazy val updatablesView = new ImmutableProperty(new ContainerView[Updatable](this))

  lazy val camera = Property[Camera](new OrthographicCamera(width, height))
  lazy val verticalSync = Property[Boolean](true)

  onUpdate(camera) {
    camera().update()
  }
  onUpdate(verticalSync) {
    Gdx.graphics.setVSync(verticalSync())
  }

  private var initialize: () => Unit = _

  /**
   * Window title
   */
  def title = getClass.getSimpleName.replaceAll("\\$", "")

  /**
   * Window width
   */
  def width = 1024

  /**
   * Window height
   */
  def height = 768

  lazy val pickFunction = (current: Component, c: Component) => if (c
      .hitTest(Mouse.x(), abs(Mouse.y() - height))) {
    c
  }
  else {
    current
  }

  private def pickComponents(evt: MouseEvent, components: Iterable[Component]): Unit = {
    val x = evt.x
    val y = abs(evt.y - height)
    val ray = camera().getPickRay(x.toFloat, y.toFloat) // TODO: use instead
    val hit = components.foldLeft(null.asInstanceOf[Component])(pickFunction)
    if (hit != null) {
      hit.mouseEvent.fire(evt.duplicate())
    }
    if (evt.isInstanceOf[MouseMoveEvent] && Component.mouse() != hit) {
      val old = Component.mouse()
      Component.mouse := hit
      if (old != null) {
        old.mouseEvent.fire(MouseOutEvent(evt.x, evt.y, evt.deltaX, evt.deltaY))
      }
      if (hit != null) {
        hit.mouseEvent.fire(MouseOverEvent(evt.x, evt.y, evt.deltaX, evt.deltaY))
      }
    }
  }

  def delayedInit(x: => Unit) = {
    size.width := width
    size.height := height

    initialize = () => x

    Mouse.mouseEvent.synchronous {
      case evt => pickComponents(evt, componentsView())
    }
  }

  final def orthographic(width: Double = this.width, height: Double = this.height) = {
    camera := new OrthographicCamera(width.toFloat, height.toFloat)
  }

  final def perspective(fov: Double = 45.0, nearPlane: Double = 0.1, farPlane: Double = 1000.0) = {
    val aspectRatio = width / height
    val c = new PerspectiveCamera(fov.toFloat, 2.0f * aspectRatio, 2.0f)
    c.near = nearPlane.toFloat
    c.far = farPlane.toFloat
    camera := c
  }

  final def main(args: Array[String]): Unit = {
    Texture.setEnforcePotImages(false) // No need to enforce power-of-two images

    // Work-around so we don't need LWJGL functionality in separate project
    val clazz = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplication")
    val constructor = clazz.getConstructor(classOf[ApplicationListener],
      classOf[String],
      classOf[Int],
      classOf[Int],
      classOf[Boolean])
    constructor.newInstance(listener,
      title,
      width.asInstanceOf[AnyRef],
      height.asInstanceOf[AnyRef],
      false.asInstanceOf[AnyRef])
  }

  private object listener extends ApplicationListener with InputProcessor {
    def create() = {
      UI.instance.set(UI.this)
      Gdx.graphics.setVSync(verticalSync())
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
      if (initialize != null) {
        initialize()
      }
    }

    def resize(width: Int, height: Int) = {
    }

    def render() = {
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT) // TODO: optional?
      delta = Gdx.graphics.getDeltaTime.toDouble
      updatablesView.value.foreach(updateUpdatables)
      camera()(Gdx.gl11)
      rendererView.value.foreach(renderRenderable)
    }

    private var delta = 0.0

    private val updateUpdatables = (updatable: Updatable) => updatable.update(delta)

    private val renderRenderable = (renderable: RenderableComponent) => renderable.render()

    def pause() = {
    }

    def resume() = {
    }

    def dispose() = {
    }

    def keyDown(keyCode: Int) = {
      Keyboard.keyEvent.fire(KeyDownEvent(Key.byKeyCode(keyCode)
          .getOrElse(throw new RuntimeException("Unknown keyCode %s".format(keyCode)))))
      true
    }

    def keyTyped(c: Char) = {
      Keyboard.keyEvent.fire(KeyTypeEvent(c))
      true
    }

    def keyUp(keyCode: Int) = {
      Keyboard.keyEvent.fire(KeyUpEvent(Key.byKeyCode(keyCode)
          .getOrElse(throw new RuntimeException("Unknown keyCode %s".format(keyCode)))))
      true
    }

    def scrolled(amount: Int) = {
      Mouse.mouseEvent.fire(MouseWheelEvent(amount, Mouse.x(), Mouse.x(), 0.0, 0.0))
      true
    }

    def touchDown(x: Int, y: Int, pointer: Int, button: Int) = {
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      Mouse.mouseEvent.fire(MousePressEvent(MouseButton(button), x, y, dx, dy))
      Mouse.x := x
      Mouse.y := y
      true
    }

    def touchDragged(x: Int, y: Int, pointer: Int) = {
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      Mouse.mouseEvent.fire(MouseDragEvent(x, y, dx, dy))
      Mouse.x := x
      Mouse.y := y
      true
    }

    def touchMoved(x: Int, y: Int) = {
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      // TODO: support deferred mouse events
      Mouse.mouseEvent.fire(MouseMoveEvent(x, y, dx, dy))
      Mouse.x := x
      Mouse.y := y
      true
    }

    def touchUp(x: Int, y: Int, pointer: Int, button: Int) = {
      val dx = abs(x - Mouse.x())
      val dy = abs(y - Mouse.y())
      Mouse.mouseEvent.fire(MouseReleaseEvent(MouseButton(button), x, y, dx, dy))
      Mouse.x := x
      Mouse.y := y
      true
    }
  }

}

object UI {
  private val instance = new ThreadLocal[UI]

  def apply() = instance.get()
}
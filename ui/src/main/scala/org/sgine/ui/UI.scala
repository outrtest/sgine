package org.sgine.ui

import com.badlogic.gdx.{InputProcessor, Gdx, ApplicationListener}
import org.sgine.scene.ContainerView
import org.sgine.input.{Mouse, MouseButton, Key, Keyboard}
import org.sgine.input.event._

import scala.math._
import org.sgine.Updatable
import org.sgine.property.{Property, ImmutableProperty}
import com.badlogic.gdx.graphics.{OrthographicCamera, Camera, Texture, GL10}

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
      Gdx.graphics.setVSync(verticalSync())
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
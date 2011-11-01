package org.sgine.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.{InputProcessor, Gdx, ApplicationListener}
import org.sgine.input.{Key, Keyboard}
import org.sgine.input.event.{KeyUpEvent, KeyTypeEvent, KeyDownEvent}
import org.sgine.property.ImmutableProperty
import org.sgine.scene.ContainerView
import com.badlogic.gdx.graphics.GL10

/**
 * UI provides a base class to be extended and allow an initialization end-point for the graphical application to start.
 *
 * The graphical context will be loaded at the time the body is evaluated, so all initialization can be done within.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class UI extends Container with DelayedInit {
  lazy val rendererView = new ImmutableProperty(new ContainerView[RenderableComponent](this))

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

  def delayedInit(x: => Unit) = {
    initialize = () => x
  }

  final def main(args: Array[String]): Unit = {
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
    lazy val batch = new SpriteBatch()

    def create() = {
      Component.batch.set(batch)
      Gdx.input.setInputProcessor(this)
      if (initialize != null) {
        initialize()
      }
    }

    def resize(width: Int, height: Int) = {
    }

    def render() = {
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT) // TODO: optional?
      batch.begin()
      rendererView.value.foreach(renderRenderable)
      batch.end()
    }

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

    def scrolled(amount: Int) = false

    def touchDown(x: Int, y: Int, pointer: Int, button: Int) = false

    def touchDragged(x: Int, y: Int, pointer: Int) = false

    def touchMoved(x: Int, y: Int) = false

    def touchUp(x: Int, y: Int, pointer: Int, button: Int) = false
  }

}
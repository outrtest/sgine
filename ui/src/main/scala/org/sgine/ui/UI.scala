package org.sgine.ui

import com.badlogic.gdx.{Gdx, ApplicationListener}
import org.sgine.property.{PropertyParent, Property}

/**
 * UI provides a base class to be extended and allow an initialization end-point for the graphical application to start.
 *
 * The graphical context will be loaded at the time the body is evaluated, so all initialization can be done within.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class UI extends PropertyParent {
  /**
   * Window title
   */
  val title = Property[String](getClass.getSimpleName)
  /**
   * Window width
   */
  val width = Property[Int](1024)
  /**
   * Window height
   */
  val height = Property[Int](768)

  title.onChange {
    if (Gdx.graphics != null) Gdx.graphics.setTitle(title())
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
      title.value,
      width.value.asInstanceOf[AnyRef],
      height.value.asInstanceOf[AnyRef],
      false.asInstanceOf[AnyRef])
  }

  private object listener extends ApplicationListener {
    def create() = {
    }

    def resize(width: Int, height: Int) = {
    }

    def render() = {
    }

    def pause() = {
    }

    def resume() = {
    }

    def dispose() = {
    }
  }

}
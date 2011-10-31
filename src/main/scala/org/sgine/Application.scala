package org.sgine

import com.badlogic.gdx.{ApplicationListener, Game}

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
class Application extends Game with DelayedInit {
  private var initialize: () => Unit = _

  def delayedInit(x: => Unit) = {
    initialize = () => x
  }

  final def create() = {
    if (initialize != null) {
      initialize()
    }
  }

  def title = getClass.getSimpleName

  def width = 800

  def height = 480

  def main(args: Array[String]): Unit = {
    // Work-around so I don't need LWJGL functionality in separate project
    val clazz = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplication")
    val constructor = clazz.getConstructor(classOf[ApplicationListener],
      classOf[String],
      classOf[Int],
      classOf[Int],
      classOf[Boolean])
    constructor.newInstance(this,
      title,
      width.asInstanceOf[AnyRef],
      height.asInstanceOf[AnyRef],
      false.asInstanceOf[AnyRef])
  }
}
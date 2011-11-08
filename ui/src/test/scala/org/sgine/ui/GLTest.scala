package org.sgine.ui

import com.badlogic.gdx.{ApplicationListener, Gdx}
import com.badlogic.gdx.graphics.GL10._
import render.{Vertex, ArrayBuffer}
import com.badlogic.gdx.graphics.{PerspectiveCamera, FPSLogger, Texture}

object GLTest extends ApplicationListener {

  import Gdx.{gl11 => gl}

  //  lazy val camera = new OrthographicCamera(1024.0f, 768.0f)
  lazy val aspectRatio = 1024.0f / 768.0f
  lazy val camera = new PerspectiveCamera(45.0f, 2f * aspectRatio, 2f)

  lazy val buffer = new ArrayBuffer(Vertex.triangle(z = -250.0, width = 150.0, height = 150.0))

  def create() = {
    Gdx.graphics.setVSync(false)
    camera.near = 0.1f
    camera.far = 1000.0f
    camera.translate(0.0f, 0.0f, 0.0f)
    camera.update()
  }

  val fps = new FPSLogger()

  def render() = {
    gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    camera(gl)

    fps.log()

    //    buffer.draw()
    buffer.bind()
    buffer.drawVertices()
  }

  def resize(width: Int, height: Int) = {
    camera.viewportWidth = width.toFloat
    camera.viewportHeight = height.toFloat
    camera.update()
  }

  def pause() = {
  }

  def resume() = {
  }

  def dispose() = {
  }

  def main(args: Array[String]): Unit = {
    Texture.setEnforcePotImages(false) // No need to enforce power-of-two images

    // Work-around so we don't need LWJGL functionality in separate project
    val clazz = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplication")
    val constructor = clazz.getConstructor(classOf[ApplicationListener],
      classOf[String],
      classOf[Int],
      classOf[Int],
      classOf[Boolean])
    constructor.newInstance(this,
      "GLTest",
      1024.asInstanceOf[AnyRef],
      768.asInstanceOf[AnyRef],
      false.asInstanceOf[AnyRef])
  }
}
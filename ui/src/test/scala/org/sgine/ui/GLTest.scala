package org.sgine.ui

import com.badlogic.gdx.{ApplicationListener, Gdx}
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.graphics.GL10._
import com.badlogic.gdx.graphics.{FPSLogger, OrthographicCamera, Texture}
import render.ArrayBuffer

object GLTest extends ApplicationListener {

  import Gdx.{gl11 => gl}

  lazy val camera = new OrthographicCamera(1024.0f, 768.0f)

  lazy val buffer = new ArrayBuffer(List(0.0, 150.0, 0.0, -150.0, -150.0, 0.0, 150.0, -150.0, 0.0))

  def create() = {
    gl.glEnable(GL_TEXTURE_2D)
    gl.glShadeModel(GL_SMOOTH)
    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    gl.glClearDepthf(1.0f)
    gl.glEnable(GL_DEPTH_TEST)
    gl.glDepthFunc(GL_LEQUAL)

    Gdx.graphics.setVSync(false)

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

  def resize(p1: Int, p2: Int) = {
    create()
  }

  def pause() {}

  def resume() {}

  def dispose() {}

  def main(args: Array[String]): Unit = {
    Texture.setEnforcePotImages(false) // No need to enforce power-of-two images

    new LwjglApplication(this, "GLTest", 1024, 768, false)
  }
}
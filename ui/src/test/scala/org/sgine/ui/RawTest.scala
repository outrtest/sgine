package org.sgine.ui

import com.badlogic.gdx.{Gdx, ApplicationListener}
import com.badlogic.gdx.graphics.{FPSLogger, OrthographicCamera, Texture, GL10}

/**
 * @author Matt Hicks <mhicks@sgine.org>
 */
object RawTest extends ApplicationListener {
  lazy val camera = new OrthographicCamera(1024, 768)
  lazy val image = Image("sgine.png")
  lazy val framerate = new FPSLogger()

  def create() = {
    Gdx.graphics.setVSync(false)
    Gdx.gl11.glShadeModel(GL10.GL_SMOOTH)
    Gdx.gl11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    Gdx.gl11.glClearDepthf(1.0f)
    Gdx.gl11.glEnable(GL10.GL_BLEND)
    Gdx.gl11.glEnable(GL10.GL_DEPTH_TEST)
    Gdx.gl11.glDepthFunc(GL10.GL_LEQUAL)
    Gdx.gl11.glEnable(GL10.GL_TEXTURE_2D)
    Gdx.gl11.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

    camera.update()
  }

  def resize(width: Int, height: Int) {}

  def render() = {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT)

    camera(Gdx.gl11)
    image.update(Gdx.graphics.getDeltaTime.toDouble)
    image.render()

    image.rotation.z := image.rotation.z() + (200.0 * Gdx.graphics.getDeltaTime)

    framerate.log()
  }

  def pause() {}

  def resume() {}

  def dispose() {}

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
      "Raw Test",
      1024.asInstanceOf[AnyRef],
      768.asInstanceOf[AnyRef],
      false.asInstanceOf[AnyRef])
  }
}

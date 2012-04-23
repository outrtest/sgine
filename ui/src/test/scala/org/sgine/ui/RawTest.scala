package org.sgine.ui

import com.badlogic.gdx.{Gdx, ApplicationListener}
import com.badlogic.gdx.graphics.{FPSLogger, OrthographicCamera, Texture, GL10}
import render.{ArrayBuffer, Vertex, TextureCoordinates}

object RawTest extends ApplicationListener {
  lazy val camera = new OrthographicCamera(1024, 768)
  lazy val texture = new Texture("sgine.png")
  lazy val framerate = new FPSLogger()

  lazy val coords = TextureCoordinates.rect()
  lazy val vertices = Vertex.rect(texture.getWidth, texture.getHeight)
  lazy val verticesLength = vertices.length
  lazy val buffer = new ArrayBuffer(vertices ::: coords)

  val deltas = new Array[Float](60)
  var position = 0
  var previous = 0.0f

  def create() = {
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

//    val delta = 1.0f / 60.0f
    val delta = Gdx.graphics.getDeltaTime
    camera.rotate(delta * 150.0f, 0.0f, 0.0f, 1.0f)
    camera.update()
    camera(Gdx.gl11)

    texture.bind()
    buffer.bind()
    buffer.bindTextureCoordinates(verticesLength)
    buffer.drawVertices(0, verticesLength / 3)

//    deltas(position) = Gdx.graphics.getDeltaTime
//    position += 1
//    if (position == deltas.length) {
//      position = 0
//      val total = deltas.foldLeft(0.0f)((total, current) => current + total)
//      val average = total / 60.0f
//      val deviation = total - previous
//      previous = total
//      println("Time: %s (%s) - Deviation: %s".format(total, average, deviation))
//    }
    framerate.log()
  }

  def pause() {}

  def resume() {}

  def dispose() {}

  def main(args: Array[String]): Unit = {
    Texture.setEnforcePotImages(false) // No need to enforce power-of-two images

    val configClass = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration")
    val config = configClass.newInstance().asInstanceOf[AnyRef]
    configClass.getField("width").set(config, 1024)
    configClass.getField("height").set(config, 768)
    configClass.getField("useGL20").set(config, false)
    configClass.getField("title").set(config, "Raw Test")

    val clazz = Class.forName("com.badlogic.gdx.backends.lwjgl.LwjglApplication")
    val constructor = clazz.getConstructor(classOf[ApplicationListener], configClass)
    constructor.newInstance(List[AnyRef](this, config): _*).asInstanceOf[com.badlogic.gdx.Application]
  }
}
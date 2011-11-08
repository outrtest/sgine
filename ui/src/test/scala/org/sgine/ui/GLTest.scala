package org.sgine.ui

import org.lwjgl.opengl.GL11._
import com.badlogic.gdx.{ApplicationListener, Gdx}
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.graphics.GL11._
import java.nio.{ByteOrder, ByteBuffer}
import com.badlogic.gdx.graphics.{FPSLogger, OrthographicCamera, Texture}

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

    buffer.draw()
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

class ArrayBuffer(vertices: Seq[Double]) {
  lazy val id = init()
  lazy val size = vertices.length * 4

  import Gdx.{gl11 => gl}

  private def init() = {
    val id = ArrayBuffer.genBuffer()
    gl.glBindBuffer(GL_ARRAY_BUFFER, id)
    gl.glBufferData(GL_ARRAY_BUFFER, size, null, GL_STATIC_DRAW)
    val buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())
    vertices.foreach(d => buffer.putFloat(d.toFloat))
    buffer.flip()
    gl.glBufferSubData(GL_ARRAY_BUFFER, 0, size, buffer)
    id
  }

  def draw() = {
    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    gl.glEnableClientState(GL_VERTEX_ARRAY)
    gl.glBindBuffer(GL_ARRAY_BUFFER, id)
    gl.glVertexPointer(3, GL_FLOAT, 0, 0)
    gl.glDrawArrays(GL_TRIANGLES, 0, vertices.size / 3)
  }
}

object ArrayBuffer {
  def genBuffer() = {
    val array = new Array[Int](1)
    Gdx.gl11.glGenBuffers(1, array, 0)
    array(0)
  }
}
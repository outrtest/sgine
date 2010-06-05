package org.sgine.render.shape

import org.sgine.core.Color

import org.sgine.render.Image

import org.lwjgl.opengl.GL11._

import scala.math._

class Ring protected(val width: Double, val height: Double, val sides: Int, val holeRatio: Double,
                     val color: Color, val image: Image) extends Shape {
  val mode = GL_QUAD_STRIP

  val vertexCount = 2 * sides + 2

  val data = {
    val array =  new Array[Double](4 * sides)
    for(k <- 0 until sides) {
      val xk = sin(2 * Pi * k / sides) * width / 2
      val yk = cos(2 * Pi * k / sides) * height / 2
      //outer vertex
      array(4 * k + 0) = xk
      array(4 * k + 1) = yk
      //inner vertex
      array(4 * k + 2) = xk * holeRatio
      array(4 * k + 3) = yk * holeRatio
    }
    array
  }

  def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  def texture(index: Int) = {
    if (image != null) {
      val xk = data(index % (2*sides) * 2) / width + 0.5 + image.x / image.texture.width
      val yk =  -data(index % (2*sides) * 2 + 1) / height + 0.5 + image.y / image.texture.height
      glTexCoord2d(xk, yk)
    }
  }

  def vertex(index: Int) = glVertex3d(data(index % (2*sides) * 2), data(index % (2*sides) * 2 + 1), 0)

  override def begin() = {
    if (image != null) {
      image.texture.bind()
    }
    super.begin()
  }
}

object Ring {
  def apply(width: Double, height: Double, sides: Int = 12, holeRatio: Double = 0.5, color: Color = Color.White, image: Image = null)
  = if (sides < 3 || holeRatio <= 0 || holeRatio >= 1) throw new IllegalArgumentException()
    else new Ring(width, height, sides, holeRatio, color, image)
}
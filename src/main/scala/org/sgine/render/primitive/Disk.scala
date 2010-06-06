/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.render.primitive

import org.sgine.core.Color

import org.sgine.render.Image

import org.lwjgl.opengl.GL11._

import scala.math._

class Disk protected(val width: Double, val height: Double, val sides: Int, val color: Color, val image: Image) extends Primitive {
  val mode = GL_TRIANGLE_FAN

  val vertexCount = sides + 2

  val data = {
    val array =  new Array[Double](2 * (sides + 2))
    array(0) = 0
    array(1) = 0
    for(k <- 0 to sides) {
      array(2 * k + 2) = sin(2 * Pi * k / sides) * width / 2
      array(2 * k + 2 + 1) = cos(2 * Pi * k / sides) * height / 2
    }
    array
  }

  def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  def texture(index: Int) = {
    if (image != null) {
      val xk = data(index * 2) / width + 0.5 + image.x / image.texture.width
      val yk =  -data(index * 2 + 1) / height + 0.5 + image.y / image.texture.height
      glTexCoord2d(xk, yk)
    }
  }

  def vertex(index: Int) = glVertex3d(data(index * 2), data(index * 2 + 1), 0)

  override def begin() = {
    if (image != null) {
      image.texture.bind()
    }
    super.begin()
  }
}

object Disk {
  def apply(width: Double, height: Double, sides: Int = 12, color: Color = Color.White, image: Image = null)
  = if (sides < 3) throw new IllegalArgumentException() else new Disk(width, height, sides, color, image)
}
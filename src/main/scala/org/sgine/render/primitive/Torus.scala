package org.sgine.render.primitive

import org.sgine.core.Color

import org.sgine.render.Image

import org.lwjgl.opengl.GL11._

import scala.math._

class Torus protected(val outerRadius: Double, val innerRadius: Double,
                      val outerSamples: Int, val innerSamples: Int,
                      val color: Color, val image: Image) extends Primitive {
  val mode = GL_QUAD_STRIP

  val vertexCount = 2 * outerSamples * (innerSamples + 1)

  val data = {
    val array = new Array[Double](3 * outerSamples * (innerSamples + 1))
    for(outer <- 0 until outerSamples; inner <- 0 to innerSamples) {
      val yi = outerRadius + cos(2 * Pi * inner / innerSamples) * innerRadius
      val xk = sin(2 * Pi * outer / outerSamples) * yi
      val yk = cos(2 * Pi * outer / outerSamples) * yi
      val zk = sin(2 * Pi * inner / innerSamples) * innerRadius
      val base = 3 * ((innerSamples + 1) * outer + inner)
      array(base + 0) = xk
      array(base + 1) = yk
      array(base + 2) = zk
    }
    array
  }

  def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  def texture(index: Int) = { 
    if (image != null) {
      val base = index / 2 + (index % 2 * (innerSamples + 1))
      val xk = (base / (innerSamples + 1)).toDouble / outerSamples
      val yk = (base % (innerSamples + 1)).toDouble / innerSamples
      glTexCoord2d(xk, yk)
    }
  }

  def vertex(index: Int) = {
      val base = (index / 2 + (index % 2 * (innerSamples + 1))) % ((innerSamples + 1) * outerSamples)
      glVertex3d(data(3 * base + 0), data(3 * base + 1), data(3 * base + 2))
    }

  override def begin() = {
    if (image != null) {
      image.texture.bind()
    }
    super.begin()
  }
}

object Torus {
  def apply(outerRadius: Double, innerRadius: Double, outerSamples: Int = 20, innerSamples: Int = 10,
            color: Color = Color.White, image: Image = null)
  =  new Torus(outerRadius, innerRadius, outerSamples, innerSamples, color, image)
}
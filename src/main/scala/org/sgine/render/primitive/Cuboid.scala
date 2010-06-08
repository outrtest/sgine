package org.sgine.render.primitive

import org.sgine.core.Color

import org.sgine.render.Image

import org.lwjgl.opengl.GL11._

import scala.math._

class Cuboid(width: Double, height: Double, depth: Double, color: Color, val image: Image)
extends Primitive {

  val mode = GL_QUADS

  val vertexCount = 6 * 4

  def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  def texture(index: Int) = { 
    if (image != null) {
      val v = index % 4
      val xk = if (v % 3 == 0) 0 else 1
      val yk = if (v <= 1) 0 else 1 
      glTexCoord2d(xk, yk)
    }
  }

  def vertex(index: Int) = {
    val plane = index / 4
    val v = index % 4
    val xb = if (plane == 0) -1 else 1
    val yb = if (plane == 2) -1 else 1
    val zb = if (plane == 4) -1 else 1
    val xk = if (plane / 2 == 0) xb else if (v % 3 == 0) -xb else xb
    val yk = if (plane / 2 == 1) yb else if (v <= 1) -yb else yb
    val zk = plane / 2 match {
      case 0 => if (v % 3 == 0) -zb else zb
      case 1 => if (v <= 1) -zb else zb 
      case 2 => zb
    }
    glVertex3d(xk*width/2, yk*height/2, zk*depth/2)
  }

  override def begin() = {
    if (image != null) {
      image.texture.bind()
    }
    super.begin()
  }
}

object Cuboid {
  def apply(width: Double, height: Double, depth: Double,
            color: Color = Color.White, image: Image = null)
  =  new Cuboid(width, height, depth, color, image)
}

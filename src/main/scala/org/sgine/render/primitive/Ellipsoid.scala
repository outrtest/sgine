/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.render.primitive

import org.sgine.core.Color

import org.sgine.render.RenderImage

import org.lwjgl.opengl.GL11._

import scala.math._

/**
 * pitchSamples are the samples from north pole to south pole
 * yawSamples are the samples going around the y-axis
 */
class Ellipsoid protected(val width: Double, val height: Double, val depth: Double,
                      val pitchSamples: Int, val yawSamples: Int,
                      val color: Color, val image: RenderImage) extends Primitive {
  val mode = GL_QUAD_STRIP

  val vertexCount = 2 * (yawSamples + 1) * pitchSamples

  val data = {
    val array = new Array[Double](3 * (pitchSamples+1) * (yawSamples + 1))
    for(pitch <- 0 to pitchSamples; yaw <- 0 to yawSamples) {
      val theta = (Pi * pitch / pitchSamples) 
      val phi = 2 * Pi * yaw / yawSamples
      val sinTheta = sin(theta)
      val base = 3 * ((yawSamples + 1) * pitch + yaw)
      array(base + 0) = width / 2 * sinTheta * cos(phi)
      array(base + 1) = height / 2 * cos(theta)
      array(base + 2) = depth / 2 * sinTheta * sin(phi)
    }
    array
  }

  def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  def texture(index: Int) = {
    if (image != null) {
      val base = index / 2 + (index % 2 * (yawSamples + 1))
      val xk = (base / (yawSamples + 1)).toDouble / pitchSamples
      val yk = (base % (yawSamples + 1)).toDouble / yawSamples
      glTexCoord2d(xk, yk)
    }
  }

  def vertex(index: Int) = {
      val base = index / 2 + (index % 2 * (yawSamples + 1))
      glVertex3d(data(3 * base + 0), data(3 * base + 1), data(3 * base + 2))
    }

  override def begin() = {
    if (image != null) {
      image.texture.bind()
    }
    super.begin()
  }
}

object Ellipsoid {
  def apply(width: Double, height: Double, depth: Double, pitchSamples: Int = 12, yawSamples: Int = 12,
            color: Color = Color.White, image: RenderImage = null)
  =  new Ellipsoid(width, height, depth, pitchSamples, yawSamples, color, image)
}
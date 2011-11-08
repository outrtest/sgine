package org.sgine.ui.render

/**
 *
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Vertex {
  def triangle(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, width: Double = 2.0,
      height: Double = 2.0) = {
    List(
      x, (y + (height / 2.0)), z, // Top point
      (x - (width / 2.0)), (y - (height / 2.0)), z, // Bottom left point
      (x + (width / 2.0)), (y - (height / 2.0)), z // Bottom right point
    )
  }

  def quad(x1: Double = -1.0, y1: Double = 1.0, x2: Double = 1.0, y2: Double = -1.0,
      z: Double = 0.0) = {
    List(
      x1, y1, 0.0, // Top left
      x2, y1, 0.0, // Top right
      x1, y2, 0.0, // Bottom left
      x2, y2, 0.0, // Bottom right
      x1, y2, 0.0, // Bottom left
      x2, y1, 0.0 // Top right
    )
  }

  def rect(width: Double, height: Double) = {
    quad((-width / 2.0), (height / 2.0), (width / 2.0), (-height / 2.0))
  }

  def rectLeft(width: Double, height: Double) = quad(0.0, height, width, 0.0)

  def box(width: Double, height: Double, depth: Double) = {
    val w = width / 2.0
    val h = height / 2.0
    val d = depth / 2.0
    List(
      // Front Face
      -w, h, d,
      w, h, d,
      -w, -h, d,
      w, -h, d,
      -w, -h, d,
      w, h, d,
      // Back Face
      -w, h, -d,
      w, h, -d,
      -w, -h, -d,
      w, -h, -d,
      -w, -h, -d,
      w, h, -d,
      // Left Face
      -w, h, -d,
      -w, h, d,
      -w, -h, -d,
      -w, -h, d,
      -w, -h, -d,
      -w, h, d,
      // Right Face
      w, h, -d,
      w, h, d,
      w, -h, -d,
      w, -h, d,
      w, -h, -d,
      w, h, d,
      // Top Face
      -w, h, -d,
      w, h, -d,
      -w, h, d,
      w, h, d,
      -w, h, d,
      w, h, -d,
      // Bottom Face
      -w, -h, -d,
      w, -h, -d,
      -w, -h, d,
      w, -h, d,
      -w, -h, d,
      w, -h, -d
    )
  }
}
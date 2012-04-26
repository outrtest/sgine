package org.sgine.ui.render

/**
 * Vertex is a convenience object for generating vertices for common scenarios.
 *
 * @author Matt Hicks <mhicks@sgine.org>
 */
object Vertex {
  /**
   * Generates a triangle centered at x, y, z with width and height supplied.
   */
  def triangle(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0, width: Double = 2.0, height: Double = 2.0) = {
    List(
      x, (y + (height / 2.0)), z, // Top point
      (x - (width / 2.0)), (y - (height / 2.0)), z, // Bottom left point
      (x + (width / 2.0)), (y - (height / 2.0)), z // Bottom right point
    )
  }

  /**
   * Generates a quad from the values supplied.
   */
  def quad(x1: Double = -1.0, y1: Double = 1.0, x2: Double = 1.0, y2: Double = -1.0, z: Double = 0.0) = {
    List(
      x1, y1, 0.0, // Top left
      x2, y1, 0.0, // Top right
      x1, y2, 0.0, // Bottom left
      x2, y2, 0.0, // Bottom right
      x1, y2, 0.0, // Bottom left
      x2, y1, 0.0 // Top right
    )
  }

  /**
   * Creates a quad centered at 0.0x0.0 with the width and height supplied.
   */
  def rect(width: Double, height: Double) = {
    quad((-width / 2.0), (height / 2.0), (width / 2.0), (-height / 2.0))
  }

  /**
   * Creates a quad up and right from zero for the width and height supplied.
   */
  def rectLeft(width: Double, height: Double) = quad(0.0, height, width, 0.0)

  /**
   * Creates a box with the width and height defined.
   */
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

  def scale9(x1: Double, y1: Double, x2: Double, y2: Double, z: Double, textureWidth: Double, textureHeight: Double, width: Double, height: Double) = {
    val sx0 = width / -2.0
    val sx1 = sx0 + x1
    val sx3 = width / 2.0
    val sx2 = sx3 - (textureWidth - x2)
    val sy0 = height / 2.0
    val sy1 = sy0 - y1
    val sy3 = height / -2.0
    val sy2 = sy3 + (textureHeight - y2)

    quad(sx0, sy0, sx1, sy1, z) :::      // Top-Left
    quad(sx1, sy0, sx2, sy1, z) :::      // Top
    quad(sx2, sy0, sx3, sy1, z) :::      // Top-Right
    quad(sx0, sy1, sx1, sy2, z) :::      // Left
    quad(sx1, sy1, sx2, sy2, z) :::      // Center
    quad(sx2, sy1, sx3, sy2, z) :::      // Right
    quad(sx0, sy2, sx1, sy3, z) :::      // Bottom-Left
    quad(sx1, sy2, sx2, sy3, z) :::      // Bottom
    quad(sx2, sy2, sx3, sy3, z)          // Bottom-Right
  }

  def clip(minX: Double, minY: Double, maxX: Double, maxY: Double, points: List[Double]) = {
    points.grouped(3).flatMap {
      case x :: y :: z :: Nil => List(clipPoint(x, minX, maxX), clipPoint(y, minY, maxY), z)
      case _ => throw new RuntimeException("Is not divisible by three!")
    }.toList
  }

  private def clipPoint(value: Double, minimum: Double, maximum: Double) = if (value < minimum) {
    minimum
  } else if (value > maximum) {
    maximum
  } else {
    value
  }
}
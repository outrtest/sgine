package org.sgine.render.primitive

import org.lwjgl.opengl.GL11._
import org.sgine.render.RenderImage
import org.sgine.math.{Vector2, Vector3}
import scala.math._
import org.sgine.core.Color

trait Projection {
  /*
   * Calculates the texture coordinates (in range 0..1) for the specified target coordinates (in range 0..1)
   */
  def textureCoordinates(x: Double, y: Double): Vector2
}

case object LinearProjection extends Projection {
  def textureCoordinates(x: Double, y: Double) = Vector2(x, y)
}

case object PolarProjection extends Projection {
  def textureCoordinates(x: Double, y: Double) = Vector2(cos(x * 2*Pi) * y, sin(x * 2*Pi) * y)
}

object TextureArea {
  val Full = TextureArea(Vector2(0,0), Vector2(1,0), Vector2(0,1), Vector2(1,1))
}

case class TextureArea(topLeft: Vector2, topRight: Vector2, bottomLeft: Vector2, bottomRight: Vector2)


object TextureMapping {
  val Default = TextureMapping()
}

/*
 * Describes a texture segment used on the cylinder and how it should be projected to it.
 * All texture segments used on a single LathePrimitive come from the same texture.
 *
 * @param textureArea The area of the texture to use
 * @param projection how the texture should be projected to the cylinder.
 * @param relativeSize How much of the target this projection should cover, relative to the others.
 * @param numberOfWraps How many times the texture segment is wrapped
 * @param siblingSegment another texture segment that is wrapped at the same height around the cylinder, sharing that height with this segment
 * @param sharp*Edge if true, the specified edge of this texture segment will not have its normals smoothed.
 */
// TODO: Allow composition of texture segments?
case class TextureMapping(textureArea: TextureArea = TextureArea.Full,
                          projection: Projection = LinearProjection,
                          sharpLeftEdge: Boolean = false,
                          sharpRightEdge: Boolean = false,
                          sharpTopEdge: Boolean = false,
                          sharpBottomEdge: Boolean = false)

/*
 * Parametric cyliner shaped primitive with a specified number of sides, segments, and a surface function.
 */
class LathePrimitive(segments: Int,
                     sides: Int,
                     surface: (Double, Double) => Vector3,
                     color: Color = Color.White,
                     texture: RenderImage,
                     textureMapping: TextureMapping = TextureMapping.Default,
                     lengthScale: Double = 1,
                     radialScale: Double = 1) extends Primitive {

  // TODO: Implement

  private var indexes: Seq[Int] = Nil
  private var vertices: Seq[Double] = Nil
  val mode = GL_TRIANGLES

  def vertexCount = indexes.length

  def color(index: Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  def texture(index: Int) = {
	}

	def vertex(index: Int) = {
		val i = 3 * indexes(index)
		glVertex3d(vertices(i), vertices(i + 1), vertices(i + 2))
	}

}
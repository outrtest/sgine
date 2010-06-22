package org.sgine.render.primitive

import org.lwjgl.opengl.GL11._
import org.sgine.render.RenderImage
import org.sgine.math.{Vector2, Vector3}
import scala.math._
import org.sgine.core.Color
import collection.immutable.SortedMap

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
  val Default = SimpleTextureMapping()
}

trait TextureMapping {

  /* Returns a map from the row sample (starting with 0) to the TextureMapping that starts at that row */
  def rows(numberOfRowSamples: Int): Map[Int, TextureMapping]

  /* Returns a map from the column sample (starting with 0) to the TextureMapping that starts at that column */
  def columns(numberOfColumnSamples: Int): Map[Int, TextureMapping]

  /* Size of this mapping in relation to the others on the same row / column */
  def relativeSize: Double

  protected def build(mappings: List[TextureMapping]): Map[Int, TextureMapping] = {
    val totalRelativeSize: Double = mappings.foldLeft(0.0)(_ + _.relativeSize)

    // Divide the texture maps between the available samples
    var location = 0.0
    var result = Map[Int, TextureMapping]()
    mappings foreach { mapping:TextureMapping =>
      val start = (location * totalRelativeSize).toInt
      location += mapping.relativeSize
      result += (start -> mapping)
    }
    result
  }
}

case class RowTextureMapping(columns: List[TextureMapping], relativeSize: Double = 1.0) extends TextureMapping {
  def rows(numberOfRowSamples: Int): Map[Int, TextureMapping] = SortedMap( 0 -> this )
  def columns(numberOfColumnSamples: Int): Map[Int, TextureMapping] = build(columns)
}

case class ColumnTextureMapping(rows: List[TextureMapping], relativeSize: Double = 1.0) extends TextureMapping  {
  def rows(numberOfRowSamples: Int): Map[Int, TextureMapping] = build(rows)
  def columns(numberOfColumnSamples: Int): Map[Int, TextureMapping] = SortedMap( 0 -> this )
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
case class SimpleTextureMapping(textureArea: TextureArea = TextureArea.Full,
                          projection: Projection = LinearProjection,
                          relativeSize: Double = 1.0,
                          sharpLeftEdge: Boolean = false,
                          sharpRightEdge: Boolean = false,
                          sharpTopEdge: Boolean = false,
                          sharpBottomEdge: Boolean = false) extends TextureMapping{

  def rows(numberOfRowSamples: Int): SortedMap[Int, TextureMapping] = SortedMap( 0 -> this )
  def columns(numberOfColumnSamples: Int): Map[Int, TextureMapping] = SortedMap( 0 -> this )

}

/*
 * Parametric cylinder shaped primitive with a specified number of sides, segments, and a surface function.
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


package org.sgine.render.primitive

import org.lwjgl.opengl.GL11._
import org.sgine.render.RenderImage
import org.sgine.math.{Vector2, Vector3}
import scala.math._
import org.sgine.core.Color
import java.util.ArrayList
import collection.immutable.{Map, SortedMap}

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


/*
 * Parametric cylinder shaped primitive with a specified number of sides, segments, and a surface function.
 */
class LathePrimitive(surface: (Double, Double) => Vector3, // TODO: Add a few useful parametrized two parameter surface functions.
                     segments: Int = 10,
                     sides: Int = 10,
                     colorFunction: (Double, Double) => Color = (u,v) => Color.White,
                     texture: RenderImage = null,
                     textureProjection: Projection = LinearProjection,
                     lengthScale: Double = 1,
                     radialScale: Double = 1) extends Primitive {

  private val meshData: MeshData = new MeshData()

  create()

  private def create() {

    var segment = 0
    while (segment < segments) {

      var side = 0
      while (side < sides) {

        val u = 1.0*side / (sides-1.0)
        val v = 1.0*segment / (segments-1.0)

        val pos = surface(u, v)
        val tex = textureProjection.textureCoordinates(u, v)
        val col = colorFunction(u, v)
        val nor = Vector3.UnitY // Calculated afterwards

        val index = meshData.addVertex(pos, nor, tex, col)

        if (segment > 0 && side > 0) meshData.addQuad(index, index-1, index -1 -sides, index-sides)
        if (segment > 0 && side == 0) meshData.addQuad(index, index + sides -1, index -1, index-sides) // Stitch together into a cylinder shape
        // TODO: Simple stitching like this will be problematic for the texture - instead a seam should be used with duplicate vertices along the seam with different texture coordinates.

        side += 1
      }

      segment += 1
    }

    meshData.smoothAllNormals()
  }

  val mode = GL_TRIANGLES

  // TODO: Rendering logic could be moved to mesh data, or MeshData merged with Mesh

  def vertexCount = meshData.nrOfIndexes

  def color(index: Int) = {
    val color = meshData.getColor(meshData.getIndex(index))
    if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)
  }

  def texture(index: Int) = {
    val texturePos = meshData.getTextureCoordinate(meshData.getIndex(index))
    if (texturePos != null) glTexCoord2d(texturePos.x, texturePos.y)
	}

	def vertex(index: Int) = {
    val pos = meshData.getVertex(meshData.getIndex(index))
		glVertex3d(pos.x, pos.y, pos.z)
	}

}


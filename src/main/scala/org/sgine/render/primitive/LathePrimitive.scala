package org.sgine.render.primitive

import org.lwjgl.opengl.GL11._
import org.sgine.render.RenderImage
import scala.math.Pi
import scala.math.cos
import scala.math.sin
import org.sgine.core.Color
import java.util.ArrayList
import collection.immutable.{Map, SortedMap}

import simplex3d.math.doublem._

trait Projection {
  /*
   * Calculates the texture coordinates (in range 0..1) for the specified target coordinates (in range 0..1)
   */
  def textureCoordinates(x: Double, y: Double): Vec2d
}

case object LinearProjection extends Projection {
  def textureCoordinates(x: Double, y: Double) = Vec2d(x, y)
}

case object PolarProjection extends Projection {
  def textureCoordinates(x: Double, y: Double) = Vec2d(cos(x * 2*Pi) * y, sin(x * 2*Pi) * y)
}

case class LinearFunction(scale: Double = 1, offset: Double = 0) extends Func.Signal {
  override def apply(v : Double) : Double = v * scale + offset
}

case class LinearInterpolator(a: Double, b: Double) extends Func.Signal {
  override def apply(t : Double) : Double = (1.0 - t) * a + t * b
}

case class ConstantFunction(value: Double = 0) extends Function1[Double, Double] {
  override def apply(v : Double) : Double = value
}

case class PathFunction(xFunc: Func.Signal, yFunc: Func.Signal, zFunc: Func.Signal) extends Func.Path {
  def apply(t: Double) : Vec3d = Vec3d(xFunc(t), yFunc(t), zFunc(t))
}

object Func {
  type Signal = Function1[Double, Double]
  type Path = Function1[Double, Vec3d]
  type Envelope = Function2[Double, Double, Vec3d]
}

object IdentityFunction extends LinearFunction(1, 0)
object OneFunction extends ConstantFunction(1)
object StraightPath extends PathFunction(ConstantFunction(), LinearFunction(), ConstantFunction())

case class CylinderFunction(radiusOverLength: Func.Signal = OneFunction,
                            radiusShape: Func.Signal = OneFunction,
                            path: Func.Path=StraightPath) extends Func.Envelope {
  override def apply(u : Double, v : Double) : Vec3d = {
    val uAsRadians: Double = u * 2 * Pi
    val radiusScale = radiusShape(u) * radiusOverLength(v)
    val x = path(v).x + cos(uAsRadians) * radiusScale
    val y = path(v).y
    val z = path(v).z + sin(uAsRadians) * radiusScale

    Vec3d(x, y, z)
  }
}

// TODO: Add a few more useful surface envelope functions, and move them to a separate package.

/*
 * Parametric cylinder shaped primitive with a specified number of sides, segments, and a surface function.
 */
class LathePrimitive(surface: Func.Envelope = CylinderFunction(),
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
        val nor = Vec3d.UnitY // Calculated afterwards

        val index = meshData.addVertex(pos, nor, tex, col)

        if (segment > 0 && side > 0) meshData.addQuad(index, index-1, index -1 -sides, index-sides)
        if (segment > 0 && side == 0) meshData.addQuad(index, index + sides -1, index -1, index-sides) // Stitch together into a cylinder shape

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


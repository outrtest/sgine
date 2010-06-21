/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sgine.render.primitive

import org.sgine.core.Color
import org.sgine.math.Vector2
import org.sgine.render.RenderImage

import java.awt.Shape
import java.awt.geom.FlatteningPathIterator
import java.awt.geom.PathIterator._

import org.lwjgl.opengl.GL11._

import scala.collection.mutable.ListBuffer
import scala.math._


class Extrusion private[primitive](val data : Seq[Vector2],
                        val extrusion : Double,
                        val color : Color,
                        val image : RenderImage) extends Primitive {

  val mode = GL_QUAD_STRIP

  val vertexCount = 2 * data.size + 2

  def color(index : Int) = if (color != null) glColor4d(color.red, color.green, color.blue, color.alpha)

  private lazy val (minY, maxY) =
    data.foldLeft((Double.MaxValue, -Double.MaxValue))((t, v) => (t._1 min v.y, t._2 max v.y))


  def texture(index : Int) = {
    if (image != null) {
      val xk = index % 2
      val yk = (maxY - data((index / 2) % data.size).y) / (maxY - minY)
      glTexCoord2d(xk, yk)
    }
  }

  def vertex(index : Int) = {
    val v = data( (index / 2) % data.size )
    glVertex3d( v.x, v.y, (index % 2) * extrusion )
  }

  override def begin() = {
    if (image != null) {
      image.texture.bind()
    }
    super.begin()
  }

}

object Extrusion {

   def apply(shape : Shape, extrusion : Double, color : Color = Color.White,
             image : RenderImage = null, flatness : Double = 0.001, limit : Int = 10) : Seq[Extrusion] = {

    val coords = Array(0f, 0f)
    val coordList = ListBuffer[Vector2]()
    val primitives = ListBuffer[Extrusion]()

    val path = new FlatteningPathIterator(shape.getPathIterator(null), flatness, limit)

    while (! path.isDone) {
      path.currentSegment(coords) match {
        case SEG_MOVETO => coordList.clear
                           coordList += Vector2(coords(0), coords(1))
        case SEG_LINETO => coordList += Vector2(coords(0), coords(1))
        case SEG_CLOSE =>  primitives += new Extrusion(coordList.toSeq, extrusion, color, image)
      }
      path.next
    }

    return primitives.toList
   }

}

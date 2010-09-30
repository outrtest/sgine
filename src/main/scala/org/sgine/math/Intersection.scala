/*
 * Copyright (c) 2010 Sgine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'Sgine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sgine.math

import simplex3d.math._
import simplex3d.math.doublem.renamed._
import simplex3d.math.doublem.DoubleMath._


/*
 * @author Aleksey Nikiforov (lex)
 */
object Main {
  def main(args: Array[String]): Unit = {
    val ray = new Ray(Vec3(-1, 1, 0), Vec3(1, 0, 1)*100)
    val res = ray.intersectObb(Vec3(0), Vec3(10), Mat3x4 scale 2 translate Vec3(1, 0, 0))
    res match {
      case Nil => println("no intersection")
      case Seq(in, out) => println(ray.point(in) + " " + ray.point(out))
    }
  }
}

/*
 * This class defines a directed line segment starting at the origin and ending
 * at (origin + direction).
 *
 * @param origin ray origin.
 * @param direction non-normalized direction.
 *
 * @author Aleksey Nikiforov (lex)
 */
class Ray(val origin: ConstVec3, val direction: ConstVec3) {
  def point(s: Double) = origin + direction*s

  def intersectsPlane(normal: inVec3, coefficient: Double) :Boolean = {
    throw new UnsupportedOperationException()
  }

  def intersectsSphere(translation: inVec3, radius: Double) :Boolean = {
    throw new UnsupportedOperationException()
  }

  // Possibly use "Separating Axis Test"
  def intersectsAabb(min: inVec3, max: inVec3) :Boolean = {
    !intersectAabb(origin, direction, min, max).isEmpty
  }

  def intersectsObb(
    min: inVec3, max: inVec3, transformation: inMat3x4
  ) :Boolean = {
    !intersectObb(min, max, transformation).isEmpty
  }

  def intersectPlane(normal: inVec3, coefficient: Double) :Seq[Double] = {
    throw new UnsupportedOperationException()
  }

  def intersectSphere(translation: inVec3, radius: Double) :Seq[Double] = {
    throw new UnsupportedOperationException()
  }

  def intersectAabb(min: inVec3, max: inVec3) :Seq[Double] = {
    intersectAabb(origin, direction, min, max)
  }

  def intersectObb(
    min: inVec3, max: inVec3, transformation: inMat3x4
  ) :Seq[Double] = {
    val t = inverse(transformation)
    intersectAabb(
      t.transformPoint(origin), t.transformVector(direction),
      min, max
    )
  }

  private def intersectAabb(
    origin: inVec3, direction: inVec3,
    min: inVec3, max: inVec3
  ) :Seq[Double] = {

    // (origin + direction*ratio(0)) is an intersecion point closest to the origin,
    // (origin + direction*ratio(1)) is the second intersection point.
    val ratio = Array[Double](0, 1)

    def testComponent(
      origin: Double, direction: Double,
      min: Double, max: Double
    ) :Boolean = {
      if (abs(direction) < 1e-14) return (min <= origin && origin <= max)

      val invDirection = 1/direction
      var rmin = (min - origin)*invDirection
      var rmax = (max - origin)*invDirection
      if (rmin > rmax) { val tmp = rmin; rmin = rmax; rmax = tmp }

      if (rmin > ratio(0)) ratio(0) = rmin
      if (rmax < ratio(1)) ratio(1) = rmax

      (ratio(0) <= ratio(1))
    }

    if (!testComponent(origin.x, direction.x, min.x, max.x)) return Nil
    if (!testComponent(origin.y, direction.y, min.y, max.y)) return Nil
    if (!testComponent(origin.z, direction.z, min.z, max.z)) return Nil

    ratio
  }
}

object FrustumTestResult extends Enumeration {
  type FrustumTestResult = Value

  val Outside = Value
  val Intersecting = Value
  val Inside = Value

  def cull(i: FrustumTestResult) = if (i == Outside) true else false
}

import FrustumTestResult._

class Frustum(
  leftNormal: ConstVec3, leftCoefficient: Double,
  rightNormal: ConstVec3, rightCoefficient: Double,
  bottomNormal: ConstVec3, bottomCoefficient: Double,
  topNormal: ConstVec3, topCoefficient: Double,
  nearNormal: ConstVec3, nearCoefficient: Double,
  farNormal: ConstVec3, farCoefficient: Double
) {
  import Frustum._

  private[this] final val normals = new Array[ConstVec3](6)
  normals(Left) = leftNormal
  normals(Right) = rightNormal
  normals(Bottom) = bottomNormal
  normals(Top) = topNormal
  normals(Near) = nearNormal
  normals(Far) = farNormal

  private[this] final val coefs = new Array[Double](6)
  coefs(Left) = leftCoefficient
  coefs(Right) = rightCoefficient
  coefs(Bottom) = bottomCoefficient
  coefs(Top) = topCoefficient
  coefs(Near) = nearCoefficient
  coefs(Far) = farCoefficient

  final def normal(i: Int) = normals(i)
  final def coefficient(i: Int) = coefs(i)


  final def intersectPoint(point: inVec3) :FrustumTestResult = {
    if (dot(point, normal(Left)) < -coefficient(Left)) Outside
    else if (dot(point, normal(Right)) < -coefficient(Right)) Outside
    else if (dot(point, normal(Bottom)) < -coefficient(Bottom)) Outside
    else if (dot(point, normal(Top)) < -coefficient(Top)) Outside
    else if (dot(point, normal(Near)) < -coefficient(Near)) Outside
    else if (dot(point, normal(Far)) < -coefficient(Far)) Outside
    else Inside
  }

  final def intersectSphere(
    translation: inVec3, radius: Double
  ) :FrustumTestResult = {
    var res = Inside

    {
      val test = dot(translation, normal(Left)) + coefficient(Left)
      if (test < -radius) return Outside else if (test < radius) res = Intersecting
    }
    {
      val test = dot(translation, normal(Right)) + coefficient(Right)
      if (test < -radius) return Outside else if (test < radius) res = Intersecting
    }
    {
      val test = dot(translation, normal(Bottom)) + coefficient(Bottom)
      if (test < -radius) return Outside else if (test < radius) res = Intersecting
    }
    {
      val test = dot(translation, normal(Top)) + coefficient(Top)
      if (test < -radius) return Outside else if (test < radius) res = Intersecting
    }
    {
      val test = dot(translation, normal(Near)) + coefficient(Near)
      if (test < -radius) return Outside else if (test < radius) res = Intersecting
    }
    {
      val test = dot(translation, normal(Far)) + coefficient(Far)
      if (test < -radius) return Outside else if (test < radius) res = Intersecting
    }

    res
  }

  final def intersectAabb(
    min: inVec3, max: inVec3
  ) :FrustumTestResult = {
    var res = Inside

    var i = 0; while (i < 6) {
      val planeNormal = normal(i)
      val planeCoef = coefficient(i)

      val pSelector = greaterThanEqual(planeNormal, Vec3.Zero)
      val pVertex = mix(min, max, pSelector)

      val pTest = dot(pVertex, planeNormal)
      if (pTest < -planeCoef) return Outside
      else {
        val nVertex = mix(min, max, not(pSelector))

        val nTest = dot(nVertex, planeNormal)
        if (nTest < -planeCoef) res = Intersecting
      }

      i += 1
    }

    res
  }

  // Redo with rotationMat only
  // and with worldTranformation by rebuilding the planes from the matrix product.
  final def intersectObb(
    min: inVec3, max: inVec3, worldTranformation: inMat3x4
  ) :FrustumTestResult = {
    var res = Inside

    val m = Mat3(worldTranformation)
    val c3 = worldTranformation(3)

    var i = 0; while (i < 6) {

      // Transform the plane equation as follows:
      // val planeEq = Vec4(normal(i), coefficient(i))
      // val transformed = planeEq*Mat4(modelMatrix)
      // val normalizedPlane = normalizePlane(transformed)
      // The code below does the same thing, but more efficiently.

      val frustumNormal = normal(i)
      val planeNormal = frustumNormal*m
      val invLen = 1/length(planeNormal)
      planeNormal *= invLen
      val planeCoef = (dot(frustumNormal, c3) + coefficient(i))*invLen

      // Continue like we have AABB.
      val pSelector = greaterThanEqual(planeNormal, Vec3.Zero)
      val pVertex = mix(min, max, pSelector)

      val pTest = dot(pVertex, planeNormal)
      if (pTest < -planeCoef) return Outside
      else {
        val nVertex = mix(min, max, not(pSelector))

        val nTest = dot(nVertex, planeNormal)
        if (nTest < -planeCoef) res = Intersecting
      }

      i += 1
    }

    res
  }

  override def toString() = {
    "Frustum(\n" +
    "  " + normal(Left) + ", " + coefficient(Left) + ",\n" +
    "  " + normal(Right) + ", " + coefficient(Right) + ",\n" +
    "  " + normal(Bottom) + ", " + coefficient(Bottom) + ",\n" +
    "  " + normal(Top) + ", " + coefficient(Top) + ",\n" +
    "  " + normal(Near) + ", " + coefficient(Near) + ",\n" +
    "  " + normal(Far) + ", " + coefficient(Far) + "\n" +
    ")"
  }
}

object Frustum {
  final val Left = 0
  final val Right = 1
  final val Bottom = 2
  final val Top = 3
  final val Near = 4
  final val Far = 5

  private def normalizePlane(nd: inVec4) :Vec4 = {
    val len = length(nd.xyz)
    nd/len
  }

  def apply(viewProjection: inMat4) :Frustum = {
    val rows = transpose(viewProjection)
    val rx = rows(0)
    val ry = rows(1)
    val rz = rows(2)
    val rw = rows(3)

    val left = normalizePlane(rw + rx)
    val right = normalizePlane(rw - rx)
    val bottom = normalizePlane(rw + ry)
    val top = normalizePlane(rw - ry)
    val near = normalizePlane(rw + rz)
    val far = normalizePlane(rw - rz)

    new Frustum(
      left.xyz, left.w,
      right.xyz, right.w,
      bottom.xyz, bottom.w,
      top.xyz, top.w,
      near.xyz, near.w,
      far.xyz, far.w
    )
  }
}
